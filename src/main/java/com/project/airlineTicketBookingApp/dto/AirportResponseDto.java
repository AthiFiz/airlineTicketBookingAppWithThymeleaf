package com.project.airlineTicketBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportResponseDto {

    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;

}
