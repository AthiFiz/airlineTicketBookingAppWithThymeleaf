package com.project.airlineTicketBookingApp.repository;

import com.project.airlineTicketBookingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);


}
