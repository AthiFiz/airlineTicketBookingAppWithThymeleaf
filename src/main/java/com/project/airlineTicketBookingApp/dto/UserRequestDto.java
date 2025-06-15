package com.project.airlineTicketBookingApp.dto;

import com.project.airlineTicketBookingApp.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank @Size(min = 6)
    private String password;

//    ROLE_CUSTOMER, ROLE_OPERATOR, ROLE_ADMIN
    @NotNull
    private Role role;

}
