package com.project.airlineTicketBookingApp.service;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;

import java.util.List;

public interface TicketService {

    TicketResponseDto bookTicket(Long flightId, Long passengerId,
                                 TicketClass ticketClass, String seatNumber);
    List<TicketResponseDto> getTicketsForFlight(Long flightId);
    List<TicketResponseDto> getTicketsForPassenger(Long passengerId);
    void cancelTicket(Integer ticketId);


}
