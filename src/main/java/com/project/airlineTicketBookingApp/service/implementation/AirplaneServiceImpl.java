package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.AirplaneRequestDto;
import com.project.airlineTicketBookingApp.dto.AirplaneResponseDto;
import com.project.airlineTicketBookingApp.model.Airplane;
import com.project.airlineTicketBookingApp.model.Airport;
import com.project.airlineTicketBookingApp.model.User;
import com.project.airlineTicketBookingApp.repository.AirplaneRepository;
import com.project.airlineTicketBookingApp.repository.AirportRepository;
import com.project.airlineTicketBookingApp.service.AirplaneService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneRepository airplaneRepo;
    private final AirportRepository airportRepo;

    public AirplaneServiceImpl(AirplaneRepository airplaneRepo, AirportRepository airportRepo) {
        this.airplaneRepo = airplaneRepo;
        this.airportRepo = airportRepo;
    }

    @Override
    @Transactional
    public AirplaneResponseDto createAirplane(AirplaneRequestDto dto) {
        String tailNumber = dto.getTailNumber().toUpperCase();
        if (airplaneRepo.existsByTailNumber(tailNumber)) {
            throw new IllegalArgumentException("Tail number already exists: " + tailNumber);
        }

        Airport home = airportRepo.findById(dto.getHomeAirportId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Home airport not found: ID " + dto.getHomeAirportId()));

        Airplane airplane = new Airplane();
        airplane.setTailNumber(tailNumber);
        airplane.setSize(dto.getSize());
        airplane.setModel(dto.getModel());
        airplane.setHomeAirport(home);
        Airplane saved = airplaneRepo.save(airplane);

        return mapToDto(saved);
    }

    @Override
    public AirplaneResponseDto getAirplaneById(Long id) {
        Airplane airplane = airplaneRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found: ID " + id));
        return mapToDto(airplane);
    }

    @Override
    public List<AirplaneResponseDto> getAllAirplanes() {
        return airplaneRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Airplane a = airplaneRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found: " + id));
        airplaneRepo.delete(a);
    }

    @Override
    public void updateAirplane(Long id, AirplaneRequestDto dto) {
        Airplane airplane = airplaneRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airplane not found: ID " + id));

        airplane.setModel(dto.getModel());
        airplane.setSize(dto.getSize());
        airplaneRepo.save(airplane);

    }

    private AirplaneResponseDto mapToDto(Airplane a) {
        return new AirplaneResponseDto(
                a.getId(),
                a.getTailNumber(),
                a.getSize(),
                a.getModel(),
                a.getHomeAirport().getId(),
                a.getHomeAirport().getCode()
        );
    }
}
