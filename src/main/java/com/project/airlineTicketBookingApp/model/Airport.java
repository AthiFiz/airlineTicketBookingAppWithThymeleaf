package com.project.airlineTicketBookingApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "airports")
@Getter @Setter
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String code; // IATA code, e.g., "CMB", "JFK"

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @OneToMany(mappedBy = "homeAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Airplane> stationedAirplanes;

}
