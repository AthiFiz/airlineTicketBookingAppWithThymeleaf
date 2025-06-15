package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.service.TicketService;
import com.project.airlineTicketBookingApp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BookingWebController {

    private final TicketService ticketService;
    private final UserService userService;

    public BookingWebController(TicketService ticketService,
                                UserService userService) {
        this.ticketService = ticketService;
        this.userService   = userService;
    }

    @GetMapping("/bookings")
    public String myBookings(@AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        // Look up the current user to get their ID
        UserResponseDto user = userService.getUserByUsername(userDetails.getUsername());

        // Fetch all tickets for that user
        List<TicketResponseDto> bookings =
            ticketService.getTicketsForPassenger(user.getId());

        model.addAttribute("bookings", bookings);
        return "booking-history";
    }

    /** Cancel a booking and redirect back */
    @PostMapping("/bookings/cancel/{ticketId}")
    public String cancelBooking(@PathVariable Integer ticketId) {
        ticketService.cancelTicket(ticketId);
        return "redirect:/bookings";
    }
}
