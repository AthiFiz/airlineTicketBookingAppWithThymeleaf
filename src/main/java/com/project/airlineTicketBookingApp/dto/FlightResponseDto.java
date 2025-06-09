package com.project.airlineTicketBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FlightResponseDto {

    private Long id;
    private String tailNumber;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int totalFirstClassSeats;
    private int totalBusinessClassSeats;
    private int totalEconomyClassSeats;

}
