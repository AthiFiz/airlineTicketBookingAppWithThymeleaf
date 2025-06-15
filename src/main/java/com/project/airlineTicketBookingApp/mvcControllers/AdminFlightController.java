package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.FlightRequestDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.service.AirplaneService;
import com.project.airlineTicketBookingApp.service.AirportService;
import com.project.airlineTicketBookingApp.service.FlightService;
import com.project.airlineTicketBookingApp.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/flights")
@Secured("ROLE_ADMIN")
public class AdminFlightController {

    private final FlightService flightService;
    private final AirplaneService airplaneService;
    private final AirportService airportService;
    private final TicketService ticketService;


    public AdminFlightController(FlightService flightService,
                                 AirplaneService airplaneService,
                                 AirportService airportService,
                                 TicketService ticketService) {
        this.flightService   = flightService;
        this.airplaneService = airplaneService;
        this.airportService  = airportService;
        this.ticketService  = ticketService;
    }

    @GetMapping
    public String list(Model model) {
        List<FlightResponseDto> all = flightService.getAllFlights();
        model.addAttribute("flights", all);
        return "admin/flight-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("flight", new FlightRequestDto());
        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        model.addAttribute("airports",  airportService.getAllAirports());
        model.addAttribute("editMode", false);
        return "admin/flight-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        FlightResponseDto dto = flightService.getFlightById(id);
        FlightRequestDto form = new FlightRequestDto();
        form.setAirplaneId(dto.getAirplaneId());
        form.setDepartureAirportCode(dto.getDepartureAirportCode());
        form.setArrivalAirportCode(dto.getArrivalAirportCode());
        form.setDepartureTime(dto.getDepartureTime());
        form.setArrivalTime(dto.getArrivalTime());
        form.setTotalFirstClassSeats(dto.getTotalFirstClassSeats());
        form.setTotalBusinessClassSeats(dto.getTotalBusinessClassSeats());
        form.setTotalEconomyClassSeats(dto.getTotalEconomyClassSeats());
        model.addAttribute("flight", form);
        model.addAttribute("editMode", true);
        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        model.addAttribute("airports",  airportService.getAllAirports());
        return "admin/flight-form";
    }

    @PostMapping
    public String save(@ModelAttribute("flight") @Valid FlightRequestDto dto,
                       BindingResult binding,
                       @RequestParam(required = false) boolean editMode,
                       Model model) {

        model.addAttribute("airplanes", airplaneService.getAllAirplanes());
        model.addAttribute("airports",  airportService.getAllAirports());
        model.addAttribute("editMode",   false);

        // Validation errors?
        if (binding.hasErrors()) {
            return "admin/flight-form";
        }

        try {
            flightService.scheduleFlight(dto);
            return "redirect:/admin/flights";
        } catch (IllegalStateException ex) {
            // Business‐rule violation: show on form
            model.addAttribute("scheduleError", ex.getMessage());
            return "admin/flight-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        flightService.deleteById(id);
        return "redirect:/admin/flights";
    }

    /** Passenger manifest for flight {id} */
    @GetMapping("/{id}/manifest")
    public String manifest(@PathVariable Long id, Model model) {
        // fetch all tickets for this flight
        List<TicketResponseDto> tickets =
                ticketService.getTicketsForFlight(id);

        model.addAttribute("tickets", tickets);
        model.addAttribute("flightId", id);
        return "admin/flight-manifest";
    }
}
