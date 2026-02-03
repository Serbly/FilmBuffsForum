package org.example.filmbuffsforum.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.CreateUserRequest;
import org.example.filmbuffsforum.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("user", userService.getUserInfo());
        return "auth/profile";
    }

    @GetMapping("/update")
    public String updatePage(Model model) {
        model.addAttribute("updateRequest", new CreateUserRequest());
        return "auth/update";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute CreateUserRequest request, Model model) {
        try {
            userService.updateUser(request);
            return "redirect:/auth/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления пользователя");
            return "auth/update";
        }
    }
}
