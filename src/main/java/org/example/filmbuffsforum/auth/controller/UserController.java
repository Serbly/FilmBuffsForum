package org.example.filmbuffsforum.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.CreateUserRequest;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.security.CurrentUser;
import org.example.filmbuffsforum.auth.service.UserService;
import org.example.filmbuffsforum.content.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/profile/{username}")
    public String profilePage(@PathVariable String username, @CurrentUser User me, Model model) {
        User user = userService.findByUsername(username);

        boolean isMe = me.getId().equals(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("isMe", isMe);
        model.addAttribute("posts", postService.findByAuthor(user));
        return "auth/profile";
    }

    @GetMapping("/update")
    public String updatePage(Model model, @CurrentUser User user) {
        model.addAttribute("updateRequest", user);
        return "auth/update";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute CreateUserRequest request, Model model) {
        try {
            userService.updateUser(request);
            return "redirect:/app/user/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления пользователя");
            return "auth/update";
        }
    }
}
