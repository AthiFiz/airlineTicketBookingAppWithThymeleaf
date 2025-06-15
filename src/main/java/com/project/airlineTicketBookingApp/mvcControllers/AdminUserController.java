package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.UserRequestDto;
import com.project.airlineTicketBookingApp.dto.UserResponseDto;
import com.project.airlineTicketBookingApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@Secured("ROLE_ADMIN")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        model.addAttribute("editMode", false);
        return "admin/user-form";
    }

    /** Handle new user submission */
    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userRequestDto") UserRequestDto dto,
            BindingResult binding,
            Model model
    ) {
        model.addAttribute("editMode", false);

        if (binding.hasErrors()) {
            return "admin/user-form";
        }
        userService.createUser(dto);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserResponseDto existing = userService.getUserById(id);

        UserRequestDto dto = new UserRequestDto();
        dto.setUsername(existing.getUsername());
        dto.setPassword("");                // require re-entry
        dto.setRole(existing.getRole());

        model.addAttribute("userRequestDto", dto);
        model.addAttribute("editMode", true);
        model.addAttribute("userId", id);
        return "admin/user-form";
    }

    @PostMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("userRequestDto") UserRequestDto dto,
            BindingResult binding,
            Model model) {

        if (binding.hasErrors()) {
            model.addAttribute("editMode", true);
            model.addAttribute("userId", id);
            return "admin/user-form";
        }
        userService.updateUser(id, dto);
        return "redirect:/admin/users";
    }


    @PostMapping("/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return "redirect:/admin/users";
    }


}
