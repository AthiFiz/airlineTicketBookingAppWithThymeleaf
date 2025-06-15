package com.project.airlineTicketBookingApp.service;

import com.project.airlineTicketBookingApp.dto.AirplaneRequestDto;
import com.project.airlineTicketBookingApp.dto.AirplaneResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AirplaneService {

    AirplaneResponseDto createAirplane(AirplaneRequestDto dto);
    AirplaneResponseDto getAirplaneById(Long id);
    List<AirplaneResponseDto> getAllAirplanes();
    void deleteById(Long id);

    void updateAirplane(Long id, AirplaneRequestDto dto);

}
