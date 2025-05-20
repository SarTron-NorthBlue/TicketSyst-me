package com.example.ticketapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Pages accessibles sans authentification
                .requestMatchers(
                    "/", 
                    "/home", 
                    "/login", 
                    "/webjars/**", 
                    "/css/**", 
                    "/js/**",
                    "/images/**"
                ).permitAll()
                
                // Toutes les autres URLs nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")  // Page de login personnalisée
                .defaultSuccessUrl("/dashboard")  // Redirection après connexion
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout")  // Redirection après déconnexion
                .permitAll()
            )
            .csrf().disable();  // Désactive CSRF pour simplifier (à réactiver en production)

        return http.build();
    }
}
