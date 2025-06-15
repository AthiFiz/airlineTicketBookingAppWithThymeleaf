package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.AirportResponseDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.service.AirportService;
import com.project.airlineTicketBookingApp.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/reports")
@Secured({"ROLE_OPERATOR","ROLE_ADMIN"})
public class ReportsController {

    private final AirportService airportService;
    private final FlightService flightService;

    public ReportsController(AirportService airportService,
                             FlightService flightService) {
        this.airportService = airportService;
        this.flightService  = flightService;
    }

    @GetMapping
    public String showReportForm(Model model) {
        List<AirportResponseDto> airports = airportService.getAllAirports();
        model.addAttribute("airports", airports);
        return "flight-reports";
    }

    @GetMapping(params = {"airportCode","startDate","endDate"})
    public String handleReport(
        @RequestParam String airportCode,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
        Model model) {

        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("selectedAirport", airportCode);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end   = endDate.atTime(LocalTime.MAX);

        List<FlightResponseDto> departures =
            flightService.getDepartures(airportCode, start, end);
        List<FlightResponseDto> arrivals =
            flightService.getArrivals(airportCode, start, end);

        model.addAttribute("departures", departures);
        model.addAttribute("arrivals", arrivals);

        return "flight-reports";
    }
}
