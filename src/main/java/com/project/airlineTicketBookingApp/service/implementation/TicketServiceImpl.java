package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.TicketResponseDto;
import com.project.airlineTicketBookingApp.model.Flight;
import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;
import com.project.airlineTicketBookingApp.model.User;
import com.project.airlineTicketBookingApp.repository.FlightRepository;
import com.project.airlineTicketBookingApp.repository.TicketRepository;
import com.project.airlineTicketBookingApp.repository.UserRepository;
import com.project.airlineTicketBookingApp.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final FlightRepository flightRepo;
    private final UserRepository userRepo;


    @Override
    @Transactional
    public TicketResponseDto bookTicket(Long flightId, Long passengerId, TicketClass ticketClass, String seatNumber) {

        // 1) Validate flight exists
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID"));

        // 2) Validate passenger exists
        User passenger = userRepo.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger ID"));

        // 3) Check seat availability by class
        long alreadyBooked = ticketRepo.countByFlightIdAndTicketClass(
                flightId, ticketClass);

        int capacity;

        switch (ticketClass) {
            case FIRST:
                capacity = flight.getTotalFirstClassSeats();
                break;
            case BUSINESS:
                capacity = flight.getTotalBusinessClassSeats();
                break;
            case ECONOMY:
                capacity = flight.getTotalEconomyClassSeats();
                break;
            default:
                throw new IllegalArgumentException("Unknown ticket class");
        }
        if (alreadyBooked >= capacity) {
            throw new IllegalStateException("No seats available in " + ticketClass);
        }

        // 4) Check seatNumber uniqueness on this flight
        if (ticketRepo.existsByFlightIdAndSeatNumber(flightId, seatNumber)) {
            throw new IllegalStateException("Seat " + seatNumber + " is already taken");
        }

        // 5) Create and save the ticket
        Ticket ticket = Ticket.builder()
                .flight(flight)
                .passenger(passenger)
                .ticketClass(ticketClass)
                .seatNumber(seatNumber)
                .build();

        Ticket saved = ticketRepo.save(ticket);

        // 6) Map to DTO
        return new TicketResponseDto(
                saved.getId(),
                flight.getId(),
                flight.getAirplane().getTailNumber(),
                passenger.getId(),
                passenger.getUsername(),
                ticketClass.name(),
                seatNumber
        );

    }

    @Override
    public List<TicketResponseDto> getTicketsForFlight(Long flightId) {

        if (flightRepo.findById(flightId).isEmpty()) {
            throw new IllegalArgumentException("Invalid flight ID");
        }

        return ticketRepo.findByFlightId(flightId)
                .stream()
                .map(t -> new TicketResponseDto(
                        t.getId(),
                        t.getFlight().getId(),
                        t.getFlight().getAirplane().getTailNumber(),
                        t.getPassenger().getId(),
                        t.getPassenger().getUsername(),
                        t.getTicketClass().name(),
                        t.getSeatNumber()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDto> getTicketsForPassenger(Long userId) {

        if (userRepo.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return ticketRepo.findByPassengerId(userId).stream()
                .map(t -> new TicketResponseDto(
                        t.getId(),
                        t.getFlight().getId(),
                        t.getFlight().getAirplane().getTailNumber(),
                        t.getPassenger().getId(),
                        t.getPassenger().getUsername(),
                        t.getTicketClass().name(),
                        t.getSeatNumber()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelTicket(Integer ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Ticket not found: " + ticketId));
        ticketRepo.delete(ticket);
    }

}
