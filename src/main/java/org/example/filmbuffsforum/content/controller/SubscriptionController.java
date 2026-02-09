package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.repository.UserRepository;
import org.example.filmbuffsforum.auth.security.CurrentUser;
import org.example.filmbuffsforum.content.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    @PostMapping("/follow/{username}")
    public void follow(@PathVariable String username, @CurrentUser User me) {
        User target = findUser(username);
        subscriptionService.follow(me, target);
    }

    @PostMapping("/unfollow/{username}")
    public void unfollow(@PathVariable String username, @CurrentUser User me) {
        User target = findUser(username);
        subscriptionService.unfollow(me, target);
    }

    @GetMapping("/status/{username}")
    public boolean status(@PathVariable String username,
                          @CurrentUser User me) {

        User target = findUser(username);
        return subscriptionService.isSubscribed(me, target);
    }

    @GetMapping("/count/{username}")
    public long count(@PathVariable String username) {
        User target = findUser(username);
        return subscriptionService.countFollowers(target);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}