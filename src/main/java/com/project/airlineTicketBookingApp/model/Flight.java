package com.project.airlineTicketBookingApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "flights",
        uniqueConstraints = @UniqueConstraint(columnNames = {"airplane_id", "departure_time"}))
@Getter @Setter
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The airplane assigned to this flight
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    // Departure and arrival airports
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    // Times (one-way ticket)
    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    // Seat capacities by class
    @Column(nullable = false)
    private int totalFirstClassSeats;

    @Column(nullable = false)
    private int totalBusinessClassSeats;

    @Column(nullable = false)
    private int totalEconomyClassSeats;

    // A set of tickets / bookings (one flight → many tickets). We'll define Ticket later.
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets;



}
