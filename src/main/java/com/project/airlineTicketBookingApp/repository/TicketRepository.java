package com.project.airlineTicketBookingApp.repository;

import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // 1) Find all tickets for a given flight (for manifest report)
    List<Ticket> findByFlightId(Long flightId);

    // 2) Find all tickets by passenger (for customer view)
    List<Ticket> findByPassengerId(Long passengerId);

    // 3) Count tickets in a given class for a flight (to check availability)
    long countByFlightIdAndTicketClass(Long flightId, TicketClass ticketClass);

    // 4) Check if a seat is already taken on a flight
    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);

}
