package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.security.CurrentUser;
import org.example.filmbuffsforum.content.dto.CreatePostRequest;
import org.example.filmbuffsforum.content.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("request", new CreatePostRequest());
        return "content/create-post";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CreatePostRequest request, @CurrentUser User user) {
        postService.create(request, user);
        return "redirect:/";
    }
}
