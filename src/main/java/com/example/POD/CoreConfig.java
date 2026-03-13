package com.example.POD;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CoreConfig {

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200","https://compilerbackend2.onrender.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // "Authorization" header ko specifically allow karna zaroori hai
                        .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin")
                        .exposedHeaders("Authorization") // Isse frontend token ko read kar payega
                        .allowCredentials(true)
                        .maxAge(3600); // Preflight (OPTIONS) request ka result cache karne ke liye

            }
        };
    }
}
