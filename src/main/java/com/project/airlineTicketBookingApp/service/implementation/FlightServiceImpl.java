package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.FlightRequestDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.model.Airplane;
import com.project.airlineTicketBookingApp.model.Airport;
import com.project.airlineTicketBookingApp.model.Flight;
import com.project.airlineTicketBookingApp.repository.AirplaneRepository;
import com.project.airlineTicketBookingApp.repository.AirportRepository;
import com.project.airlineTicketBookingApp.repository.FlightRepository;
import com.project.airlineTicketBookingApp.service.FlightService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepo;
    private final AirplaneRepository airplaneRepo;
    private final AirportRepository airportRepo;

    public FlightServiceImpl(FlightRepository flightRepo, AirplaneRepository airplaneRepo, AirportRepository airportRepo) {
        this.flightRepo = flightRepo;
        this.airplaneRepo = airplaneRepo;
        this.airportRepo = airportRepo;
    }

    @Override
    @Transactional
    public FlightResponseDto scheduleFlight(FlightRequestDto dto) {

        // 1) Lookup airplane
        Airplane plane = airplaneRepo.findById(dto.getAirplaneId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid airplane ID: " + dto.getAirplaneId()));

        // 2) Lookup departure/arrival airports by code
        Airport depAirport = airportRepo.findByCode(dto.getDepartureAirportCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid departure airport code: " + dto.getDepartureAirportCode()));

        Airport arrAirport = airportRepo.findByCode(dto.getArrivalAirportCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid arrival airport code: " + dto.getArrivalAirportCode()));

        // 3) Check plane is stationed and no overlap (same as before)…
        if (!plane.getHomeAirport().getId().equals(depAirport.getId())) {
            throw new IllegalStateException("Airplane not at departure airport");
        }

        boolean overlap = flightRepo.existsOverlappingFlight(plane.getId(), dto.getDepartureTime(), dto.getArrivalTime());
        if (overlap) {
            throw new IllegalStateException("Overlapping flight for this airplane");
        }

        // 4) Map DTO → entity
        Flight flight = new Flight();
        flight.setAirplane(plane);
        flight.setDepartureAirport(depAirport);
        flight.setArrivalAirport(arrAirport);
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setTotalFirstClassSeats(dto.getTotalFirstClassSeats());
        flight.setTotalBusinessClassSeats(dto.getTotalBusinessClassSeats());
        flight.setTotalEconomyClassSeats(dto.getTotalEconomyClassSeats());

        Flight saved = flightRepo.save(flight);

        // 5) Map entity → response DTO
        return mapToDto(saved);
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        return flightRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightResponseDto> searchDirect(String originCode, String destCode,
                                                LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepo.findDirectFlights(originCode.toUpperCase(),
                        destCode.toUpperCase(),
                        startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


//    @Override
//    public List<TransitFlightOption> searchTransit(String originCode, String destCode,
//                                                   LocalDateTime startDate, LocalDateTime endDate) {
//        // Re-use existing logic, but you can choose whether to wrap f1/f2 in DTOs too.
//        // For brevity, we leave TransitFlightOption containing full Flight entities.
//        return flightRepo.findByDepartureAirport_CodeAndDepartureTimeBetween(
//                        originCode.toUpperCase(), startDate, endDate
//                )
//                .stream()
//                .flatMap(f1 -> {
//                    String inter = f1.getArrivalAirport().getCode();
//                    LocalDateTime earliest = f1.getArrivalTime().plusHours(1);
//                    LocalDateTime latest   = f1.getArrivalTime().plusHours(6);
//
//                    List<Flight> seconds = flightRepo
//                            .findByDepartureAirport_CodeAndArrivalAirport_CodeAndDepartureTimeBetween(
//                                    inter, destCode.toUpperCase(), earliest, latest);
//
//                    return seconds.stream()
//                            .map(f2 -> new TransitFlightOption(f1, f2));
//                })
//                .collect(Collectors.toList());
//    }


    @Override
    public List<FlightResponseDto> getDepartures(String airportCode, LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepo.findDepartures(airportCode.toUpperCase(), startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightResponseDto> getArrivals(String airportCode, LocalDateTime startDate, LocalDateTime endDate) {
        return flightRepo.findArrivals(airportCode.toUpperCase(), startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Helper to convert Flight entity → FlightResponseDto
    private FlightResponseDto mapToDto(Flight f) {
        return new FlightResponseDto(
                f.getId(),
                f.getAirplane().getTailNumber(),
                f.getDepartureAirport().getCode(),
                f.getArrivalAirport().getCode(),
                f.getDepartureTime(),
                f.getArrivalTime(),
                f.getTotalFirstClassSeats(),
                f.getTotalBusinessClassSeats(),
                f.getTotalEconomyClassSeats()
        );
    }
}
