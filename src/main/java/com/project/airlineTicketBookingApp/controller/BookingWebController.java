package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.model.TicketClass;
import com.project.airlineTicketBookingApp.service.TicketService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingWebController {

    private final TicketService ticketService;
    public BookingWebController(TicketService ticketService) { this.ticketService = ticketService; }

    @PostMapping(path = "/tickets/book", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String bookFromForm(
            @RequestParam Long flightId,
            @RequestParam Long passengerId,
            @RequestParam TicketClass ticketClass,
            @RequestParam String seatNumber,
            Model model) {

        try {
            TicketResponseDto created = ticketService.bookTicket(flightId, passengerId, ticketClass, seatNumber);
            model.addAttribute("bookingResult", "Success! Ticket ID: " + created.getId());
        } catch (Exception ex) {
            model.addAttribute("bookingResult", "Error: " + ex.getMessage());
        }
        model.addAttribute("flightId", flightId);
        return "booking-form";
    }

}
