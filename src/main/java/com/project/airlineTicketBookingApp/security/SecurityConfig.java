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
public class SecurityConfig {

    private final CustomUserDetailsService uds;

    public SecurityConfig(CustomUserDetailsService uds) {
        this.uds = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                Disable CSRF for H2 console
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
//                Allow frames from same origin (H2 console uses frames)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
//                        public
                        .requestMatchers("/", "/login", "/flights/search", "/flights/search/**",
                                "/flights/search/transit", "/css/**", "/js/**").permitAll()
//                        booking form (UI) requires login
                        .requestMatchers("/flights/book").authenticated()
//                        booking action requires CUSTOMER or OPERATOR
                        .requestMatchers("/tickets/book").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_OPERATOR", "ROLE_ADMIN")
//                        reports
                        .requestMatchers("/reports/**").hasAnyAuthority("ROLE_OPERATOR","ROLE_ADMIN")
//                        admin UI
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                        any other request requires auth
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

        // Register custom UserDetailsService
        http.userDetailsService(uds);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return uds;
    }
}
