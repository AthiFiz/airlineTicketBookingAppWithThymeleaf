package com.project.airlineTicketBookingApp.service;


import com.project.airlineTicketBookingApp.dto.AirportRequestDto;
import com.project.airlineTicketBookingApp.dto.AirportResponseDto;

import java.util.List;

public interface AirportService {

    AirportResponseDto createAirport(AirportRequestDto dto);
    AirportResponseDto getAirportByCode(String code);
    List<AirportResponseDto> getAllAirports();
    void deleteByCode(String code);
    AirportResponseDto updateAirport(AirportRequestDto dto);

}
