package com.project.airlineTicketBookingApp.dto;

import com.project.airlineTicketBookingApp.model.AircraftSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneResponseDto {

    private Long id;
    private String tailNumber;
    private AircraftSize size;
    private String model;
    private Long homeAirportId;
    private String homeAirportCode;

}
