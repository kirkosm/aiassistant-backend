package com.chatbot.aiassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Απενεργοποιούμε την προστασία CSRF (χρήσιμο για APIs χωρίς φόρμες)
                .csrf(csrf -> csrf.disable())

                // Ορίζουμε ποια endpoints είναι δημόσια και ποια απαιτούν authentication
                    .authorizeHttpRequests(auth -> auth
                        // Επιτρέπουμε πλήρως την πρόσβαση στα endpoints για login/signup και chat
                        .requestMatchers("/api/auth/**", "/api/chat/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
