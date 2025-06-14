package com.project.airlineTicketBookingApp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightRequestDto {

    @NotNull
    private Long airplaneId;

    @NotNull @Size(min = 3, max = 3)
    private String departureAirportCode;

    @NotNull @Size(min = 3, max = 3)
    private String arrivalAirportCode;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    private int totalFirstClassSeats;

    private int totalBusinessClassSeats;

    private int totalEconomyClassSeats;
}
