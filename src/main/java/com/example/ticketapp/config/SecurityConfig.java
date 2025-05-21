package com.example.ticketapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
                    "/auth/**",  // Autorise toutes les URLs sous /auth
                    "/webjars/**", 
                    "/css/**", 
                    "/js/**",
                    "/images/**",
                    "/error"
                ).permitAll()
                
                // Toutes les autres URLs nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")  // Page de login personnalisée
                .loginProcessingUrl("/auth/perform_login")  // URL de traitement
                .successHandler(successHandler())
                .failureUrl("/auth/login?error=true")  // Redirection en cas d'échec
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/perform_logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf.disable());  // Désactive CSRF pour simplifier (à réactiver en production)

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                com.example.ticketapp.model.User user = (com.example.ticketapp.model.User) authentication.getPrincipal();
                return "/dashboard/" + user.getCompanySlug();
            }
        };
        handler.setUseReferer(false);
        return handler;
    }

    // Ajoutez cette méthode pour fournir un bean PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}