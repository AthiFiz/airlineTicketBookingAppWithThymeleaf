package com.project.airlineTicketBookingApp.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {~

    private final CustomUserDetailsService uds;

    public SecurityConfig(CustomUserDetailsService uds) {
        this.uds = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DelegatingPasswordEncoder that supports {noop}, {bcrypt}, etc.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for H2 console
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                // Allow frames from same origin (H2 console uses frames)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Public pages
                        .requestMatchers("/", "/login", "/flights/search", "/flights/search/**", "/css/**").permitAll()
                        // Booking endpoints
                        .requestMatchers("/tickets/book").hasAnyAuthority("ROLE_CUSTOMER","ROLE_OPERATOR")
                        .requestMatchers("/tickets/passenger/**").hasAnyAuthority("ROLE_CUSTOMER","ROLE_OPERATOR")
                        // Operator reports
                        .requestMatchers("/api/flights/report/**").hasAnyAuthority("ROLE_OPERATOR","ROLE_ADMIN")
                        // Admin only
                        .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/flights/search", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        // Register our custom UserDetailsService
        http.userDetailsService(uds);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return uds;
    }
}
