package org.example.filmbuffsforum.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.CreateUserByAdminRequest;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "auth/admin/dashboard";
    }

    @GetMapping("/dashboard/create")
    public String createPage(Model model) {
        model.addAttribute("request", new CreateUserByAdminRequest());
        model.addAttribute("editMode", false);
        return "auth/admin/create-user";
    }

    @PostMapping("/dashboard/create")
    public String createUser(@ModelAttribute CreateUserByAdminRequest request, Model model) {
        try {
            adminService.createUserByAdmin(request);
            return "redirect:/app/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка создания пользователя");
            return "auth/admin/create-user";
        }
    }

    @PostMapping("/dashboard/delete/{username}")
    public String deleteUser(@PathVariable String username, Model model) {
        try {
            adminService.deleteUserByUsername(username);
            return "redirect:/app/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("Error", "Ошибка удаления пользователя");
            return "auth/admin/dashboard";
        }
    }

    @PostMapping("/dashboard/restore/{username}")
    public String restoreUser(@PathVariable String username, Model model) {
        try {
            adminService.restoreUserByUsername(username);
            return "redirect:/app/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("Error", "Ошибка восстановления пользователя");
            return "auth/admin/dashboard";
        }
    }

    @GetMapping("/dashboard/update/{username}")
    public String updatePage(@PathVariable String username, Model model) {
        User user = adminService.getUserByUsername(username);

        CreateUserByAdminRequest request = CreateUserByAdminRequest.builder()
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();

        model.addAttribute("request", request);
        model.addAttribute("editMode", true);
        return "auth/admin/create-user";
    }

    @PostMapping("/dashboard/update")
    public String updateUser(
            @ModelAttribute CreateUserByAdminRequest request,
            Model model
    ) {
        try {
            adminService.updateUser(request);
            return "redirect:/app/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления пользователя");
            model.addAttribute("editMode", true);
            return "auth/admin/create-user";
        }
    }
}
