package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.model.TicketClass;
import com.project.airlineTicketBookingApp.service.TicketService;
import com.project.airlineTicketBookingApp.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
@Validated
public class TicketWebController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketWebController(TicketService ticketService,
                               UserService userService) {
        this.ticketService = ticketService;
        this.userService   = userService;
    }

    @PostMapping("/book")
    public String bookTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long flightId,
            @RequestParam(required = false) Long passengerId,
            @RequestParam TicketClass ticketClass,
            @RequestParam String seatNumber,
            Model model) {

        // Always re-show the flightId (so the form still renders after an error)
        model.addAttribute("flightId", flightId);

        // Determine if the current user can book for others (is an OPERATOR or ADMIN)
        var authorities = userDetails.getAuthorities();
        boolean canBookForOthers = authorities.contains(new SimpleGrantedAuthority("ROLE_OPERATOR")) ||
                authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // If the user can't book for others, or if they can but didn't select a passenger,
        // then book the ticket for the currently logged-in user.
        if (!canBookForOthers || passengerId == null) {
            UserResponseDto userResponseDto = userService.getUserByUsername(userDetails.getUsername());
            passengerId = userResponseDto.getId();
        }

        try {
            TicketResponseDto ticket = ticketService.bookTicket(
                    flightId, passengerId, ticketClass, seatNumber
            );
            model.addAttribute("ticket", ticket);
        } catch (IllegalStateException ex) {
            // Seat already taken (or other business-rule violation)
            model.addAttribute("bookingError", ex.getMessage());
        }

        // If operator, re-populate the customer dropdown
        if (canBookForOthers) {
            model.addAttribute("allCustomers", userService.getAllUsers());
        }

        return "booking-form";
    }

    /**
     * Cancel a ticket and redirect back to the bookings page.
     */
    @PostMapping("/cancel/{ticketId}")
    public String cancelBooking(@PathVariable Integer ticketId) {
        ticketService.cancelTicket(ticketId);
        return "redirect:/bookings";
    }
}
