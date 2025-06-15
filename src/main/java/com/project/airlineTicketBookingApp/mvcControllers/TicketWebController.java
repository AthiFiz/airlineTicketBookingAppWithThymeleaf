package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.model.TicketClass;
import com.project.airlineTicketBookingApp.service.TicketService;
import com.project.airlineTicketBookingApp.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        model.addAttribute("flightId", flightId);

        var authorities = userDetails.getAuthorities();
        boolean canBookForOthers = authorities.contains(new SimpleGrantedAuthority("ROLE_OPERATOR")) ||
                authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

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
            model.addAttribute("bookingError", ex.getMessage());
        }

        if (canBookForOthers) {
            model.addAttribute("allCustomers", userService.getAllUsers());
        }

        return "booking-form";
    }

    @Secured({ "ROLE_CUSTOMER", "ROLE_OPERATOR", "ROLE_ADMIN" })
    @PostMapping("/cancel/{ticketId}")
    public String cancelTicket(
            @PathVariable Integer ticketId,
            @RequestParam(required = false) Long flightId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes attrs
    ) {
        ticketService.cancelTicket(ticketId);
        attrs.addFlashAttribute("successMessage", "Ticket #" + ticketId + " cancelled.");

        boolean isOpOrAdmin = userDetails.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_OPERATOR")) ||
                userDetails.getAuthorities().contains(
                        new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isOpOrAdmin && flightId != null) {
            return "redirect:/admin/flights/" + flightId + "/manifest";
        }

        String username = userDetails.getUsername();
        return "redirect:/tickets/passenger/" + username;
    }

    @Secured({ "ROLE_OPERATOR","ROLE_ADMIN" })
    @GetMapping("/edit/{id}")
    public String editTicketForm(@PathVariable Integer id, Model model) {
        TicketResponseDto t = ticketService.getTicketById(id);
        model.addAttribute("ticket", t);
        model.addAttribute("classes", TicketClass.values());
        return "ticket-edit-form";
    }

    @Secured({ "ROLE_OPERATOR","ROLE_ADMIN" })
    @PostMapping("/edit/{id}")
    public String updateTicket(
            @PathVariable Integer id,
            @RequestParam TicketClass ticketClass,
            @RequestParam String seatNumber,
            @RequestParam Long flightId
    ) {
        ticketService.updateTicket(id, ticketClass, seatNumber, flightId);
        return "redirect:/admin/flights/" + flightId + "/manifest";
    }

    @Secured({ "ROLE_CUSTOMER","ROLE_OPERATOR","ROLE_ADMIN" })
    @GetMapping("/passenger/{username}")
    public String listPassengerBookings(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails,
            Authentication auth,
            Model model
    ) {

        boolean isSelf = userDetails.getUsername().equals(username);
        boolean isPrivileged = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OPERATOR")
                        || a.getAuthority().equals("ROLE_ADMIN"));
        if (!isSelf && !isPrivileged) {
            return "error/403";  // or your “access denied” page
        }

        UserResponseDto customer = userService.getUserByUsername(username);
        model.addAttribute("customer", customer);
        model.addAttribute("bookings",
                ticketService.getTicketsForPassenger(customer.getId()));
        return "booking-history";
    }

}
