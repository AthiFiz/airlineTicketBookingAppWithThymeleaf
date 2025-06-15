package com.project.airlineTicketBookingApp.security;

import com.project.airlineTicketBookingApp.model.User;
import com.project.airlineTicketBookingApp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {

        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name().substring(5))
                .disabled(!user.isActive())                 // ← disabled if not active
                .build();
    }
}
