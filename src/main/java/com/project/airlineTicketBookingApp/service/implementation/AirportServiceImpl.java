package com.project.airlineTicketBookingApp.service.implementation;

import com.project.airlineTicketBookingApp.dto.AirportRequestDto;
import com.project.airlineTicketBookingApp.dto.AirportResponseDto;
import com.project.airlineTicketBookingApp.model.Airport;
import com.project.airlineTicketBookingApp.repository.AirportRepository;
import com.project.airlineTicketBookingApp.service.AirportService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepo;

    public AirportServiceImpl(AirportRepository airportRepo) {
        this.airportRepo = airportRepo;
    }

    @Override
    @Transactional
    public AirportResponseDto createAirport(AirportRequestDto dto) {
        String code = dto.getCode().toUpperCase();
        if (airportRepo.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Airport code already exists: " + code);
        }
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setName(dto.getName());
        airport.setCity(dto.getCity());
        airport.setCountry(dto.getCountry());
        Airport saved = airportRepo.save(airport);
        return mapToDto(saved);
    }

    @Override
    public AirportResponseDto getAirportByCode(String code) {
        Airport airport = airportRepo.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Airport not found: " + code));
        return mapToDto(airport);
    }

    @Override
    public List<AirportResponseDto> getAllAirports() {
        return airportRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByCode(String code) {
        Airport airport = airportRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Airport not found: " + code));
        airportRepo.delete(airport);
    }

    @Override
    public AirportResponseDto updateAirport(AirportRequestDto dto) {
        Airport airport = airportRepo.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Airport not found: " + dto.getCode()));

        airport.setName(dto.getName());
        airport.setCity(dto.getCity());
        airport.setCountry(dto.getCountry());
        Airport saved = airportRepo.save(airport);
        return mapToDto(saved);
    }

    private AirportResponseDto mapToDto(Airport a) {
        return new AirportResponseDto(
                a.getId(),
                a.getCode(),
                a.getName(),
                a.getCity(),
                a.getCountry()
        );
    }
}
