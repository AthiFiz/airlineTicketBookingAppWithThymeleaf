package com.project.airlineTicketBookingApp.repository;

import com.project.airlineTicketBookingApp.model.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

    boolean existsByTailNumber(String tailNumber);

}
