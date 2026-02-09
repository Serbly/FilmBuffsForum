package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.security.CurrentUser;
import org.example.filmbuffsforum.content.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Integer> toggle(@PathVariable Long postId, @CurrentUser User user) {
        return ResponseEntity.ok(likeService.toggleLike(postId, user));
    }
}
