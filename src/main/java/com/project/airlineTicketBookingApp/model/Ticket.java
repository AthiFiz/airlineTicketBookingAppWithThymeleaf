package com.project.airlineTicketBookingApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flight_id", "seat_number"}))
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User passenger;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketClass ticketClass;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;
}
