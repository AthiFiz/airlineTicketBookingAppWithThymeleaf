package com.project.airlineTicketBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 *   - firstLeg  : Origin → IntermediateAirport
 *   - secondLeg : Same IntermediateAirport → Destination
 */

@Data
@AllArgsConstructor
public class TransitFlightOption {
    private FlightResponseDto firstLeg;
    private FlightResponseDto secondLeg;
    private final long layoverMinutes;
}
