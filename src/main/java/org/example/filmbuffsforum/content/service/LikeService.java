package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Like;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.example.filmbuffsforum.content.repository.forum.LikeRepository;
import org.example.filmbuffsforum.content.repository.forum.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public int toggleLike(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeRepository.findByPostAndUser(post, user)
                .map(like -> {
                    likeRepository.delete(like);
                    return likeRepository.findByPost(post).size();
                })
                .orElseGet(() -> {
                    Like like = new Like();
                    like.setPost(post);
                    like.setUser(user);
                    likeRepository.save(like);
                    return likeRepository.findByPost(post).size();
                });
    }
}
