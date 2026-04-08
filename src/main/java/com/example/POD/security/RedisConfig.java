package com.example.POD.security;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.util.ArrayList;
import java.util.List;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());

        // Use GenericJackson2JsonRedisSerializer with a custom ObjectMapper
        // to handle LocalDateTime and other Java 8 types.
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(createObjectMapper());

        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer jsonSerializer = 
                new GenericJackson2JsonRedisSerializer(createObjectMapper());

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(jsonSerializer)
                );

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register modules for Java 8 Time, JDK 8, etc.
        mapper.registerModule(new JavaTimeModule());
        
        // Register custom deserializer for Spring Data Page objects
        SimpleModule module = new SimpleModule();
        PageDeserializer pageDeserializer = new PageDeserializer();
        
        // Register for both interface and implementation
        module.addDeserializer(Page.class, pageDeserializer);
        
        // To register a JsonDeserializer<Page> for PageImpl.class, we need a cast.
        // We use a double cast to avoid raw type warnings while acknowledging the type gap.
        @SuppressWarnings("unchecked")
        JsonDeserializer<PageImpl<?>> pageImplDeser = (JsonDeserializer<PageImpl<?>>) (JsonDeserializer<?>) pageDeserializer;
        module.addDeserializer(PageImpl.class, pageImplDeser);
      
        mapper.registerModule(module);

        // Important for Page serialization/deserialization if using GenericJackson2JsonRedisSerializer
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Enable default typing so Jackson knows which class to deserialize into (for @class property)
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        
        return mapper;
    }

    // Custom Deserializer for PageImpl to handle construction during Redis read
    public static class PageDeserializer extends JsonDeserializer<Page<?>> {
        @Override
        public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) 
                throws java.io.IOException {
            JsonNode node = p.getCodec().readTree(p);
            
            List<Object> content = new ArrayList<>();
            if (node.has("content")) {
                JsonNode contentNode = node.get("content");
                
                // GenericJackson2JsonRedisSerializer often wraps lists with class metadata
                if (contentNode.isArray()) {
                    if (contentNode.size() == 2 && contentNode.get(0).isTextual() && contentNode.get(1).isArray()) {
                        contentNode = contentNode.get(1);
                    }
                    
                    for (JsonNode item : contentNode) {
                        Object itemValue = ctxt.readTreeAsValue(item, Object.class);
                        if (itemValue != null) {
                            content.add(itemValue);
                        }
                    }
                }
            }

            int number = node.has("number") ? node.get("number").asInt() : 0;
            int size = node.has("size") ? node.get("size").asInt() : (content.isEmpty() ? 10 : content.size());
            long totalElements = node.has("totalElements") ? node.get("totalElements").asLong() : content.size();
            
            return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
        }
    }
}




