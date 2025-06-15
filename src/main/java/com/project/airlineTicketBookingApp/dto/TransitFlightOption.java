package com.project.airlineTicketBookingApp.dto;

import com.project.airlineTicketBookingApp.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
