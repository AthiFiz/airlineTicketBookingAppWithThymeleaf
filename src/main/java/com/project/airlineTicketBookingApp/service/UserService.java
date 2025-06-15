package com.project.airlineTicketBookingApp.service;

import com.project.airlineTicketBookingApp.dto.UserRequestDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserByUsername(String username);
    UserResponseDto deactivateUser(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto dto);
    void activateUser(Long id);
}
