package org.example.filmbuffsforum.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.*;
import org.example.filmbuffsforum.auth.security.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String loginUser(
            @ModelAttribute LoginRequest request,
            HttpServletResponse response,
            Model model
    ) {
        try {

            AuthResponse auth = securityService.authenticateUser(request);

            Cookie access = new Cookie("JWT", auth.getToken());
            access.setHttpOnly(true);
            access.setPath("/");
            access.setMaxAge(15 * 60); // 15 минут

            Cookie refresh = new Cookie("refreshToken", auth.getRefreshToken());
            refresh.setHttpOnly(true);
            refresh.setPath("/");
            refresh.setMaxAge((int) auth.getRefreshTtl());

            response.addCookie(access);
            response.addCookie(refresh);

            return "redirect:/app/user/profile/" + request.getUsername();

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
    public String logout(HttpServletResponse response) {

        Cookie access = new Cookie("JWT", null);
        access.setMaxAge(0);
        access.setPath("/");

        Cookie refresh = new Cookie("refreshToken", null);
        refresh.setMaxAge(0);
        refresh.setPath("/");

        response.addCookie(access);
        response.addCookie(refresh);

        SecurityContextHolder.clearContext();

        return "redirect:/";

    }
}
