package com.project.airlineTicketBookingApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "airplanes")
@Getter @Setter
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tailNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AircraftSize size;

    @Column(nullable = false)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_airport_id", nullable = false)
    private Airport homeAirport;

}
