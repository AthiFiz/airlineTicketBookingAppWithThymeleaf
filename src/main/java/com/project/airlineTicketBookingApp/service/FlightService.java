package com.project.airlineTicketBookingApp.service;

import com.project.airlineTicketBookingApp.dto.FlightRequestDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.dto.TransitFlightOption;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {

    FlightResponseDto scheduleFlight(FlightRequestDto dto);

    List<FlightResponseDto> getAllFlights();

    List<FlightResponseDto> searchDirect(String originCode,
                                         String destCode,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate);

    List<TransitFlightOption> searchTransit(String originCode,
                                            String destCode,
                                            LocalDateTime startDate,
                                            LocalDateTime endDate);

    List<FlightResponseDto> getDepartures(String airportCode,
                                          LocalDateTime startDate,
                                          LocalDateTime endDate);

    List<FlightResponseDto> getArrivals(String airportCode,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate);

    void deleteById(Long id);

    FlightResponseDto getFlightById(Long id);

}
