package com.example.POD.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Saari CORS settings ab sirf yahan rahengi
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origins specify karein (Wildcard '*' use nahi karna agar credentials true hain)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://compiler-testcase.vercel.app"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Saare required headers jo CoreConfig mein the
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin"
        ));

        // Credentials ko allow karein (Cookies/Auth Headers ke liye)
        configuration.setAllowCredentials(true);

        // Frontend ko Authorization header read karne dein
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        // Pre-flight request cache karne ke liye
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                // MVC ki jagah Security wala CORS use hoga
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // OPTIONS (Pre-flight) ko hamesha allow karein
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/api/User/login",
                                "/api/User/addUser"

                        ).permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/teacher/**").hasAnyRole("TEACHER","ADMIN")
                        .requestMatchers("/api/student/**").hasAnyRole("STUDENT","TEACHER","ADMIN")
                        .requestMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")
                        .requestMatchers("/api/User/**").hasAnyRole("STUDENT","TEACHER","ADMIN")
                        .requestMatchers("/api/faculty/**").hasAnyRole("TEACHER","ADMIN")
                        .requestMatchers("/api/student/**").hasAnyRole("STUDENT","TEACHER","ADMIN")
                        .requestMatchers("/placement/**").hasAnyRole("TEACHER","ADMIN")
                        .requestMatchers("/api/code/**").hasAnyRole("TEACHER","STUDENT","ADMIN")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}