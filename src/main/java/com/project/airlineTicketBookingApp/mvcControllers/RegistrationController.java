package com.project.airlineTicketBookingApp.mvcControllers;

import com.project.airlineTicketBookingApp.dto.UserRequestDto;
import com.project.airlineTicketBookingApp.model.Role;
import com.project.airlineTicketBookingApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

  private final UserService userService;

  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showForm(Model model) {
    model.addAttribute("userRequestDto", new UserRequestDto());
    return "register";
  }

  @PostMapping("/register")
  public String register(
          @ModelAttribute("userRequestDto") @Valid UserRequestDto dto,
          BindingResult binding,
          Model model
  ) {
    if (binding.hasErrors()) {
      return "register";
    }
    try {
      userService.createUser(dto);
      return "redirect:/login?registered";
    } catch (IllegalStateException ex) {
      model.addAttribute("registrationError", ex.getMessage());
      return "register";
    }
  }

}
