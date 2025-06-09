package com.project.airlineTicketBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponseDto {

    private Long id;
    private Long flightId;
    private String flightTailNumber;
    private Long passengerId;
    private String passengerUsername;
    private String ticketClass;
    private String seatNumber;
}
