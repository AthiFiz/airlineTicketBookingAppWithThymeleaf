package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.AirplaneRequestDto;
import com.project.airlineTicketBookingApp.dto.AirplaneResponseDto;
import com.project.airlineTicketBookingApp.model.AircraftSize;
import com.project.airlineTicketBookingApp.service.AirplaneService;
import com.project.airlineTicketBookingApp.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/airplanes")
@Secured("ROLE_ADMIN")
public class AdminAirplaneController {

    private final AirplaneService airplaneService;
    private final AirportService  airportService;

    public AdminAirplaneController(AirplaneService airplaneService,
                                   AirportService airportService) {
        this.airplaneService = airplaneService;
        this.airportService  = airportService;
    }

    @GetMapping
    public String list(Model model) {
        List<AirplaneResponseDto> all = airplaneService.getAllAirplanes();
        model.addAttribute("airplanes", all);
        return "admin/airplane-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("airplaneDto", new AirplaneRequestDto());
        model.addAttribute("editMode", false);
        model.addAttribute("sizes", Arrays.asList(AircraftSize.values()));
        model.addAttribute("airports", airportService.getAllAirports());
        return "admin/airplane-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("airplaneDto") AirplaneRequestDto dto,
            BindingResult binding,
            Model model) {

        // repopulate the same attributes on validation errors
        model.addAttribute("airplaneDto", dto);
        model.addAttribute("editMode", false);
        model.addAttribute("sizes", Arrays.asList(AircraftSize.values()));
        model.addAttribute("airports", airportService.getAllAirports());

        if (binding.hasErrors()) {
            return "admin/airplane-form";
        }
        airplaneService.createAirplane(dto);
        return "redirect:/admin/airplanes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        AirplaneResponseDto existing = airplaneService.getAirplaneById(id);
        AirplaneRequestDto dto = new AirplaneRequestDto();
        dto.setTailNumber(existing.getTailNumber());
        dto.setSize(existing.getSize());
        dto.setModel(existing.getModel());
        dto.setHomeAirportId(existing.getHomeAirportId());

        model.addAttribute("airplaneDto", dto);
        model.addAttribute("editMode", true);
        model.addAttribute("airplaneId", id);
        model.addAttribute("sizes", Arrays.asList(AircraftSize.values()));
        model.addAttribute("airports", airportService.getAllAirports());
        return "admin/airplane-form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("airplaneDto") AirplaneRequestDto dto,
            BindingResult binding,
            Model model) {

        model.addAttribute("airplaneDto", dto);
        model.addAttribute("editMode", true);
        model.addAttribute("airplaneId", id);
        model.addAttribute("sizes", Arrays.asList(AircraftSize.values()));
        model.addAttribute("airports", airportService.getAllAirports());

        if (binding.hasErrors()) {
            return "admin/airplane-form";
        }
        airplaneService.updateAirplane(id, dto);
        return "redirect:/admin/airplanes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        airplaneService.deleteById(id);
        return "redirect:/admin/airplanes";
    }

}
