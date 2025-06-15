package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.UserRequestDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.exception.UserAlreadyExistException;
import com.project.airlineTicketBookingApp.model.User;
import com.project.airlineTicketBookingApp.repository.UserRepository;
import com.project.airlineTicketBookingApp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already taken");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword("{noop}" + dto.getPassword());
        user.setRole(dto.getRole());
        User saved = userRepository.save(user);
        return mapToDto(saved);

    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return mapToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(x -> mapToDto(x)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setActive(false);
        userRepository.save(user);

        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRole(dto.getRole());
        userRepository.save(user);
        return mapToDto(user);
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setActive(true);
    }

    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isActive());
    }
}
