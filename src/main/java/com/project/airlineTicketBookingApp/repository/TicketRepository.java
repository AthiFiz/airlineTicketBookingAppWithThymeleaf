package com.project.airlineTicketBookingApp.repository;

import com.project.airlineTicketBookingApp.model.Ticket;
import com.project.airlineTicketBookingApp.model.TicketClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByFlightId(Long flightId);

    List<Ticket> findByPassengerId(Long passengerId);

    long countByFlightIdAndTicketClass(Long flightId, TicketClass ticketClass);

    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);

}
