package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.security.CurrentUser;
import org.example.filmbuffsforum.content.model.forum.Comment;
import org.example.filmbuffsforum.content.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.getByPost(postId);
    }

    @PostMapping("/{postId}")
    public Comment addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            @RequestParam(required = false) Long parentId,
            @CurrentUser User user
            ) {
        return commentService.addComment(postId, content, parentId, user);
    }
}
