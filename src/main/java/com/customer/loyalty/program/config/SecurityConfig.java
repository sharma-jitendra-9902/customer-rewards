package com.customer.loyalty.program.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()         // Public actuator access
                .requestMatchers("/api/**").permitAll()              // Public API access
                .anyRequest().authenticated()                        // All others require auth
            )
            .httpBasic(Customizer.withDefaults())                    // Enable basic auth
            .csrf(csrf -> csrf.disable());                           // Disable CSRF for APIs

        return http.build();
    }
}
