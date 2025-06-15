package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.FlightRequestDto;
import com.project.airlineTicketBookingApp.dto.FlightResponseDto;
import com.project.airlineTicketBookingApp.dto.TransitFlightOption;
import com.project.airlineTicketBookingApp.model.Airplane;
import com.project.airlineTicketBookingApp.model.Airport;
import com.project.airlineTicketBookingApp.model.Flight;
import com.project.airlineTicketBookingApp.repository.AirplaneRepository;
import com.project.airlineTicketBookingApp.repository.AirportRepository;
import com.project.airlineTicketBookingApp.repository.FlightRepository;
import com.project.airlineTicketBookingApp.service.FlightService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

//        Lookup airplane
        Airplane plane = airplaneRepo.findById(dto.getAirplaneId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid airplane ID: " + dto.getAirplaneId()));

//        Lookup departure/arrival airports by code
        Airport depAirport = airportRepo.findByCode(dto.getDepartureAirportCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid departure airport code: " + dto.getDepartureAirportCode()));

        Airport arrAirport = airportRepo.findByCode(dto.getArrivalAirportCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid arrival airport code: " + dto.getArrivalAirportCode()));

//        Check plane is stationed and no overlap (same as before)…
        if (!plane.getHomeAirport().getId().equals(depAirport.getId())) {
            throw new IllegalStateException("Airplane not at departure airport");
        }

        boolean overlap = flightRepo.existsOverlappingFlight(plane.getId(), dto.getDepartureTime(), dto.getArrivalTime());
        if (overlap) {
            throw new IllegalStateException("Overlapping flight for this airplane");
        }

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


    @Override
    public List<TransitFlightOption> searchTransit(String originCode,
                                                   String destCode,
                                                   LocalDateTime start,
                                                   LocalDateTime end) {

//        fetch all possible first legs (all departures from the origin airport)
        List<FlightResponseDto> firstLegs = getDepartures(originCode, start, end);

//        fetch all possible second legs (all arrivals to the destination airport)
        List<FlightResponseDto> secondLegs = getArrivals(destCode, start, end);

        long minLayover = Duration.ofHours(1).toMinutes();   // >= 1h
        long maxLayover = Duration.ofHours(6).toMinutes();   // <= 6h

        List<TransitFlightOption> results = new ArrayList<>();
        for (FlightResponseDto f1 : firstLegs) {
            for (FlightResponseDto f2 : secondLegs) {
//                must connect at same airport
                if (!f1.getArrivalAirportCode().equals(f2.getDepartureAirportCode())) {
                    continue;
                }
//                The second leg must depart after the first leg arrives
                if (!f2.getDepartureTime().isAfter(f1.getArrivalTime())) {
                    continue;
                }

//                compute layover
                long layover = Duration.between(
                                f1.getArrivalTime(),
                                f2.getDepartureTime())
                        .toMinutes();
                if (layover < minLayover || layover > maxLayover) {
                    continue;
                }
                results.add(new TransitFlightOption(f1, f2, layover));
            }
        }
        return results;
    }


    @Override
    @Transactional
    public List<FlightResponseDto> getDepartures(String airportCode,
                                                 LocalDateTime start,
                                                 LocalDateTime end) {
        return flightRepo.findByDepartureAirport_CodeAndDepartureTimeBetween(
                        airportCode, start, end
                ).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<FlightResponseDto> getArrivals(String airportCode,
                                               LocalDateTime start,
                                               LocalDateTime end) {
        return flightRepo.findByArrivalAirport_CodeAndArrivalTimeBetween(
                        airportCode, start, end
                ).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        Flight f = flightRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID: " + id));
        flightRepo.delete(f);
    }

    @Override
    @Transactional
    public FlightResponseDto getFlightById(Long id) {
        Flight f = flightRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        return mapToDto(f);
    }

    public FlightResponseDto mapToDto(Flight f) {
        return new FlightResponseDto(
                f.getId(),
                f.getAirplane().getId(),
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
