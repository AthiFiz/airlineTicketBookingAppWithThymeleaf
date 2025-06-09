package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.FlightRequestDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<FlightResponseDto> createFlight(
            @RequestBody @Valid FlightRequestDto dto) {
        FlightResponseDto saved = flightService.scheduleFlight(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        List<FlightResponseDto> all = flightService.getAllFlights();
        return ResponseEntity.ok(all);
    }

    // Search direct flights
    @GetMapping("/search/direct")
    public ResponseEntity<List<FlightResponseDto>> searchDirect(
            @RequestParam String originCode,
            @RequestParam String destCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {
        List<FlightResponseDto> results =
                flightService.searchDirect(originCode, destCode, startDate, endDate);
        return ResponseEntity.ok(results);
    }

//    // Search transit itineraries
//    @GetMapping("/search/transit")
//    public ResponseEntity<List<TransitFlightOption>> searchTransit(
//            @RequestParam String originCode,
//            @RequestParam String destCode,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//            LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//            LocalDateTime endDate) {
//        List<TransitFlightOption> results =
//                flightService.searchTransit(originCode, destCode, startDate, endDate);
//        return ResponseEntity.ok(results);
//    }

    // Departures report
    @GetMapping("/report/departures")
    public ResponseEntity<List<FlightResponseDto>> getDepartures(
            @RequestParam String airportCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {
        List<FlightResponseDto> departures =
                flightService.getDepartures(airportCode, startDate, endDate);
        return ResponseEntity.ok(departures);
    }

    // Arrivals report
    @GetMapping("/report/arrivals")
    public ResponseEntity<List<FlightResponseDto>> getArrivals(
            @RequestParam String airportCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {
        List<FlightResponseDto> arrivals =
                flightService.getArrivals(airportCode, startDate, endDate);
        return ResponseEntity.ok(arrivals);
    }

}
