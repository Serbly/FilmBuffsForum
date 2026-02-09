package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.content.service.FeedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/")
    public String feed(Model model) {
        model.addAttribute("posts", feedService.getFeed());
        return "content/feed";
    }
}