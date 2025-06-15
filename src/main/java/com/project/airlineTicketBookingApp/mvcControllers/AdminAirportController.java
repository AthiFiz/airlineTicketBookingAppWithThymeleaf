package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.AirportRequestDto;
import com.project.airlineTicketBookingApp.dto.AirportResponseDto;
import com.project.airlineTicketBookingApp.service.AirplaneService;
import com.project.airlineTicketBookingApp.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/airports")
@Secured("ROLE_ADMIN")
public class AdminAirportController {

    private final AirplaneService airplaneService;
    private final AirportService airportService;

    public AdminAirportController(AirportService airportService, AirplaneService airplaneService) {

        this.airplaneService = airplaneService;
        this.airportService = airportService;
    }

    /** List all airports */
    @GetMapping
    public String list(Model model) {
        List<AirportResponseDto> all = airportService.getAllAirports();
        model.addAttribute("airports", all);
        return "admin/airport-list";
    }

    /** Show form to add a new airport */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("airport", new AirportRequestDto());
        model.addAttribute("editMode", false);
        return "admin/airport-form";
    }

    /** Show form to edit an existing airport */
    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable String code, Model model) {
        AirportResponseDto dto = airportService.getAirportByCode(code);
        AirportRequestDto form = new AirportRequestDto();
        form.setCode(dto.getCode());
        form.setName(dto.getName());
        form.setCity(dto.getCity());
        form.setCountry(dto.getCountry());
        model.addAttribute("airport", form);
        model.addAttribute("editMode", true);
        return "admin/airport-form";
    }

    /** Handle form submission for both create & update */
    @PostMapping
    public String save(
            @ModelAttribute("airport") @Valid AirportRequestDto dto,
            BindingResult binding,
            @RequestParam(required = false) boolean editMode,
            Model model) {

        model.addAttribute("airport", dto);
        model.addAttribute("editMode", editMode);

        if (binding.hasErrors()) {
            model.addAttribute("editMode", editMode);
            return "admin/airport-form";
        }

        if (editMode) {
            // for update, you might implement an updateAirport() method
            airportService.updateAirport(dto); // assuming create replaces on code
        } else {
            airportService.createAirport(dto);
        }
        return "redirect:/admin/airports";
    }

    /** Delete an airport */
    @GetMapping("/delete/{code}")
    public String delete(@PathVariable String code) {
        airportService.deleteByCode(code);
        return "redirect:/admin/airports";
    }
}
