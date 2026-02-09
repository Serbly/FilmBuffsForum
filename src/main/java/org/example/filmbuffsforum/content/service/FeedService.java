package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.content.dto.PostResponse;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.example.filmbuffsforum.content.repository.forum.CommentRepository;
import org.example.filmbuffsforum.content.repository.forum.LikeRepository;
import org.example.filmbuffsforum.content.repository.forum.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public List<PostResponse> getFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::map)
                .toList();
    }

    public PostResponse map(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .movie(post.getMovie())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likes(likeRepository.findByPost(post))
                .comments(commentRepository.findByPostId(post.getId()))
                .build();
    }
}
