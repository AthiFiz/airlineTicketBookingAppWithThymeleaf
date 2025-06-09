package com.project.airlineTicketBookingApp.dto;

import com.project.airlineTicketBookingApp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private Role role;



}
