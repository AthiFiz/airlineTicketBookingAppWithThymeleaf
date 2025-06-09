package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
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

    public FlightWebController(FlightService flightService) {
        this.flightService = flightService;
    }

    // Render the empty search form
    @GetMapping("/search")
    public String showSearchForm() {
        return "flight-search";
    }

    // Handle the form submission when all three params are present
    @GetMapping(value = "/search", params = {"originCode","destCode","date"})
    public String handleSearch(
            @RequestParam String originCode,
            @RequestParam String destCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(LocalTime.MAX);

        List<FlightResponseDto> results =
                flightService.searchDirect(originCode, destCode, start, end);

        model.addAttribute("searchResults", results);
        return "flight-search";
    }
}
