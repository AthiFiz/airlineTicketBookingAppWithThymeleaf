package com.project.airlineTicketBookingApp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobaleExceptionHandler {


    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistException ex){

        log.warn("User already exists - {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("Message", "User Already Exists");

        return ResponseEntity.badRequest().body(errors);
    }

//    @ExceptionHandler(Exception.class)
//    public String catchAll(Exception ex, Model model) {
//        model.addAttribute("message", ex.getMessage());
//        return "error";
//    }


}
