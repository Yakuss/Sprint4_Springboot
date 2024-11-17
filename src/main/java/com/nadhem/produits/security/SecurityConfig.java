package com.nadhem.produits.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Security configuration for REST API with Keycloak integration.
 * Implements CORS, CSRF protection, and role-based access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    KeycloakRoleConverter keycloakRoleConverter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configure session management
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Disable CSRF protection for REST API
            .csrf(csrf -> csrf.disable())
            
            // Configure CORS for Angular frontend
            .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                    cors.setAllowedMethods(Collections.singletonList("*"));
                    cors.setAllowedHeaders(Collections.singletonList("*"));
                    cors.setExposedHeaders(Collections.singletonList("Authorization"));
                    return cors;
                }
            }))
            
            // Configure authorization rules
            .authorizeHttpRequests(requests -> requests
                // Read access for both ADMIN and USER
                .requestMatchers("/api/all/**").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/api/getbyid/**").hasAnyAuthority("ADMIN", "USER")
                // Write access only for ADMIN
                .requestMatchers(HttpMethod.POST, "/api/addprod/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/updateprod/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/delprod/**").hasAuthority("ADMIN")
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            
            // Configure OAuth2 resource server with Keycloak
            .oauth2ResourceServer(ors -> ors.jwt(jwt ->
                jwt.jwtAuthenticationConverter(keycloakRoleConverter)));
        
        return http.build();
    }
}