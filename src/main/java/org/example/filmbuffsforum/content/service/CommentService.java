package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Comment;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.example.filmbuffsforum.content.repository.forum.CommentRepository;
import org.example.filmbuffsforum.content.repository.forum.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getByPost(Long postId) {

        List<Comment> all = commentRepository.findByPostId(postId);

        Map<Long, Comment> map = all.stream()
                .collect(Collectors.toMap(Comment::getId, c -> c));

        List<Comment> roots = new ArrayList<>();

        for (Comment c : all) {
            if (c.getParent() != null) {
                Comment parent = map.get(c.getParent().getId());
                parent.getReplies().add(c);
            } else {
                roots.add(c);
            }
        }

        return roots;
    }

    public Comment addComment(Long postId, String content, Long parentId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId).orElseThrow();
            comment.setParent(parent);
        }

        return commentRepository.save(comment);
    }
}
