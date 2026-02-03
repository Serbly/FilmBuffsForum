package org.example.filmbuffsforum.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.AuthResponse;
import org.example.filmbuffsforum.auth.dto.CreateUserRequest;
import org.example.filmbuffsforum.auth.security.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final SecurityService securityService;

    @GetMapping("/signin")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new CreateUserRequest());
        return "auth/signin";
    }

    @PostMapping("/signin")
    public String loginUser(@ModelAttribute CreateUserRequest request, Model model) {
        try {
            AuthResponse response = securityService.authenticateUser(request);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Неверный логин или пароль");
            return "auth/signin";
        }
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute CreateUserRequest request, Model model) {
        try {
            securityService.createUser(request);
            return "redirect:/auth/signin";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации");
            return "auth/signup";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/signin";
    }
}
