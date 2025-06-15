package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.AirportResponseDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.dto.TransitFlightOption;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.service.AirportService;
import com.project.airlineTicketBookingApp.service.FlightService;
import com.project.airlineTicketBookingApp.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/flights")
public class FlightWebController {

    private final FlightService flightService;
    private final AirportService airportService;
    private final UserService userService;

    public FlightWebController(FlightService flightService, AirportService airportService, UserService userService) {
        this.flightService = flightService;
        this.airportService = airportService;
        this.userService = userService;
    }

    // Render the empty search form
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        // Add all airports to the model for dropdowns
        List<AirportResponseDto> airports = airportService.getAllAirports();
        model.addAttribute("airports", airports);
        return "flight-search";
    }

    // Handle the form submission when all three params are present
    @GetMapping(value = "/search", params = {"originCode","destCode","date"})
    public String handleSearch(
            @RequestParam String originCode,
            @RequestParam String destCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        // Still need the airports list for redisplay
        model.addAttribute("airports", airportService.getAllAirports());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(LocalTime.MAX);

        List<FlightResponseDto> results =
                flightService.searchDirect(originCode, destCode, start, end);

        model.addAttribute("searchResults", results);
        return "flight-search";
    }

    // Show the empty transit-search form
    @GetMapping("/search/transit")
    public String showTransitForm() {
        return "flight-transit";
    }

    // Handle the form submission with originCode, destCode, date
    @GetMapping(value="/search/transit", params={"originCode","destCode","date"})
    public String handleTransitSearch(
            @RequestParam String originCode,
            @RequestParam String destCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(LocalTime.MAX);

        List<TransitFlightOption> opts =
                flightService.searchTransit(originCode, destCode, start, end);

        model.addAttribute("transitResults", opts);
        return "flight-transit";
    }

    /** Show booking form; include all customers if operator */
    @GetMapping("/book")
    public String showBookingForm(@RequestParam Long flightId,
                                  Authentication auth,
                                  Model model) {
        model.addAttribute("flightId", flightId);

        // Check if the user is an Operator or an Admin
        boolean canBookForOthers = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OPERATOR") ||
                        a.getAuthority().equals("ROLE_ADMIN"));

        // If they can book for others, add the list of all customers to the model
        if (canBookForOthers) {
            List<UserResponseDto> allCustomers = userService.getAllUsers();
            model.addAttribute("allCustomers", allCustomers);
        }

        return "booking-form";
    }


}
