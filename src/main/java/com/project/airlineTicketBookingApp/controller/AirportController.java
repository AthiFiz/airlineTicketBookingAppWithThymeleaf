package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.AirportRequestDto;
import com.project.airlineTicketBookingApp.dto.AirportResponseDto;
import com.project.airlineTicketBookingApp.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    /** Create a new airport */
    @PostMapping
    public ResponseEntity<AirportResponseDto> createAirport(
            @RequestBody @Valid AirportRequestDto dto) {
        AirportResponseDto created = airportService.createAirport(dto);
        return ResponseEntity.ok(created);
    }

    /** List all airports */
    @GetMapping
    public ResponseEntity<List<AirportResponseDto>> getAllAirports() {
        List<AirportResponseDto> all = airportService.getAllAirports();
        return ResponseEntity.ok(all);
    }

    /** Get one airport by its IATA code */
    @GetMapping("/code/{code}")
    public ResponseEntity<AirportResponseDto> getByCode(@PathVariable String code) {
        AirportResponseDto airport = airportService.getAirportByCode(code);
        return ResponseEntity.ok(airport);
    }


}
