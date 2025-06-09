package com.project.airlineTicketBookingApp.controller;

import com.project.airlineTicketBookingApp.dto.AirplaneRequestDto;
import com.project.airlineTicketBookingApp.dto.AirplaneResponseDto;
import com.project.airlineTicketBookingApp.service.AirplaneService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @PostMapping
    public ResponseEntity<AirplaneResponseDto> createAirplane(
            @RequestBody @Valid AirplaneRequestDto airplaneRequestDto) {
        AirplaneResponseDto created = airplaneService.createAirplane(airplaneRequestDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<AirplaneResponseDto>> getAllAirplanes() {
        List<AirplaneResponseDto> all = airplaneService.getAllAirplanes();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirplaneResponseDto> getById(@PathVariable Long id) {
        AirplaneResponseDto airplane = airplaneService.getAirplaneById(id);
        return ResponseEntity.ok(airplane);
    }


}
