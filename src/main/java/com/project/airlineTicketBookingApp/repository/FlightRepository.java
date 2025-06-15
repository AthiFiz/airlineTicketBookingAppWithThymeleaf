package com.project.airlineTicketBookingApp.repository;

import com.project.airlineTicketBookingApp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    // 1) Find direct flights between origin and destination within a date range
    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureAirport.code = :originCode " +
            "  AND f.arrivalAirport.code = :destCode " +
            "  AND f.departureTime >= :startDate " +
            "  AND f.departureTime < :endDate")
    List<Flight> findDirectFlights(@Param("originCode") String originCode,
                                   @Param("destCode") String destCode,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate")   LocalDateTime endDate);

    // 2) Find flights by airplane and overlapping times (to detect scheduling conflicts)
    @Query("SELECT CASE WHEN COUNT(f)>0 THEN TRUE ELSE FALSE END FROM Flight f " +
            "WHERE f.airplane.id = :airplaneId " +
            "  AND (" +
            "    (:depTime BETWEEN f.departureTime AND f.arrivalTime) OR " +
            "    (:arrTime BETWEEN f.departureTime AND f.arrivalTime) OR " +
            "    (f.departureTime BETWEEN :depTime AND :arrTime)" +
            "  )")
    boolean existsOverlappingFlight(@Param("airplaneId") Long airplaneId,
                                    @Param("depTime") LocalDateTime depTime,
                                    @Param("arrTime") LocalDateTime arrTime);

    // List all flights departing from a given airport (by IATA code) between two date-times
    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureAirport.code = :code " +
            "  AND f.departureTime >= :startDate " +
            "  AND f.departureTime < :endDate")
    List<Flight> findDepartures(
            @Param("code") String airportCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")   LocalDateTime endDate);

    // List all flights arriving at a given airport (by IATA code) between two date-times
    @Query("SELECT f FROM Flight f " +
            "WHERE f.arrivalAirport.code = :code " +
            "  AND f.arrivalTime >= :startDate " +
            "  AND f.arrivalTime < :endDate")
    List<Flight> findArrivals(
            @Param("code") String airportCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")   LocalDateTime endDate);

//    For transit flights

//    Find all flights departing a given airport between startDate and endDate
    List<Flight> findByDepartureAirport_CodeAndDepartureTimeBetween(
            String departureAirportCode,
            LocalDateTime startDate,
            LocalDateTime endDate);

//    // Find all flights departing from a specific airport code → arriving at destCode
//    // whose departureTime is between startDate2 and endDate2
//    List<Flight> findByDepartureAirport_CodeAndArrivalAirport_CodeAndDepartureTimeBetween(
//            String depCode,
//            String arrCode,
//            LocalDateTime startDate2,
//            LocalDateTime endDate2);

    List<Flight> findByArrivalAirport_CodeAndArrivalTimeBetween(
            String code, LocalDateTime start, LocalDateTime end);


}
