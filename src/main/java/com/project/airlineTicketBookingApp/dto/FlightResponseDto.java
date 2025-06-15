package com.project.airlineTicketBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponseDto {

    private Long id;
    private Long airplaneId;
    private String airplaneTailNumber;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int totalFirstClassSeats;
    private int totalBusinessClassSeats;
    private int totalEconomyClassSeats;

}
