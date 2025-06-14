package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;
import com.project.airlineTicketBookingApp.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

//    Book a ticket
    @PostMapping("/book")
    public ResponseEntity<TicketResponseDto> bookTicket(@RequestParam Long flightId,
                                        @RequestParam Long passengerId,
                                        @RequestParam TicketClass ticketClass,
                                        @RequestParam String seatNumber) {

        TicketResponseDto created = ticketService.bookTicket(flightId, passengerId, ticketClass, seatNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

//    Get passenger manifest for a flight
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<TicketResponseDto>> getManifest(@PathVariable Long flightId) {
        List<TicketResponseDto> ticketsDto = ticketService.getTicketsForFlight(flightId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketsDto);
    }

//    Get all tickets booked by a specific user
    @GetMapping("/passenger/{userId}")
    public ResponseEntity<List<TicketResponseDto>> getBookingsByUser(@PathVariable Long userId) {
        List<TicketResponseDto> ticketList = ticketService.getTicketsForPassenger(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketList);
    }

}
