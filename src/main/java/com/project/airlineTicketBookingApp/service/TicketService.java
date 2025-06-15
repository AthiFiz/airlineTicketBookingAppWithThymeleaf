package com.project.airlineTicketBookingApp.service;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TicketService {

    TicketResponseDto bookTicket(Long flightId, Long passengerId,
                                 TicketClass ticketClass, String seatNumber);
    List<TicketResponseDto> getTicketsForFlight(Long flightId);
    TicketResponseDto getTicketById(Integer ticketId);
    List<TicketResponseDto> getTicketsForPassenger(Long passengerId);
    void cancelTicket(Integer ticketId);
    TicketResponseDto updateTicket(Integer ticketId,
                                   TicketClass ticketClass,
                                   String seatNumber,
                                   Long flightId);


}
