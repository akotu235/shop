package io.github.akotu235.shop.controller;


import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.user.UserService;
import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.service.user.projection.UserWriteModel;
import io.github.akotu235.shop.service.user.service.EmailCheckerService;
import io.github.akotu235.shop.service.user.validator.UserValidator;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Controller
public class UserController {
    private final UserService userService;
    private final EmailCheckerService emailCheckerService;
    private final UserValidator userValidator;

    public UserController(UserService userService, EmailCheckerService emailCheckerService, UserValidator userValidator) {
        this.userService = userService;
        this.emailCheckerService = emailCheckerService;
        this.userValidator = userValidator;
    }

    @GetMapping("/register")
    public ModelAndView registration(Model model) {
        model.addAttribute("user", new UserWriteModel());
        return new ModelAndView("register-form");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(
            Model model,
            Locale locale,
            @ModelAttribute("user") @Valid UserWriteModel user,
            BindingResult bindingResult) {
        user.setLocale(locale);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-form");
        }
        Result<User> result = userService.createUser(user);
        model.addAttribute("result", result);
        return new ModelAndView("result");
    }

    @GetMapping("/registration")
    public ModelAndView emailConfirmation(
            Model model,
            @RequestParam("token") String token) {
        Result<User> result = emailCheckerService.confirm(token);
        model.addAttribute("result", result);
        return new ModelAndView("result");
    }

    @GetMapping("/user")
    public RedirectView redirect(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return new RedirectView("/user/" + authentication.getName());
        }
        return new RedirectView("/login");
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("#username == authentication.name")
    public ModelAndView userPanel(
            Model model,
            @PathVariable String username) {
        model.addAttribute("username", username);
        return new ModelAndView("user");
    }
}