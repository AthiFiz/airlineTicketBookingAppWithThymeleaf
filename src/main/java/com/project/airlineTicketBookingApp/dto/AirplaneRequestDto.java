package com.project.airlineTicketBookingApp.dto;

import com.project.airlineTicketBookingApp.model.AircraftSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneRequestDto {

    @NotBlank
    private String tailNumber;

    @NotNull
    private AircraftSize size;

    @NotBlank
    private String model;

    @NotNull
    private Long homeAirportId;  // reference to an existing Airport

}
