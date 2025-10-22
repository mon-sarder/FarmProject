package com.example.queueinv;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Configures Spring Security, defines the password encoder, and sets HTTP rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * This is the master bean for encoding passwords. We use BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This bean is required for our custom /api/auth/login endpoint.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * This is the main security configuration filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection, common for stateless REST APIs.
                // For session-based auth (which this is), you might want to enable it.
                // But for a simple JSON API, disabling is easier to start.
                .csrf(AbstractHttpConfigurer::disable)

                // This configures the authorization rules for HTTP requests
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the /api/auth/** endpoints (register, login)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(POST, "/api/auth/register").permitAll()
                        .requestMatchers(POST, "/api/auth/login").permitAll()

                        // All other /api/** requests must be authenticated
                        .requestMatchers("/api/**").authenticated()

                        // Deny any other request that doesn't match
                        .anyRequest().denyAll()
                )

                // When an unauthenticated user tries to access a protected resource,
                // send a 401 Unauthorized status code instead of redirecting to a login page.
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                )

                // This explicitly enables session-based authentication
                .securityContext(context -> context
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                );

        return http.build();
    }
}
