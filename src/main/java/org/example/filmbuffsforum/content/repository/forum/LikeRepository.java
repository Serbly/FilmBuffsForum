package org.example.filmbuffsforum.content.repository.forum;

import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Like;
import org.example.filmbuffsforum.content.model.forum.LikeId;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findByPost(Post post);

    Optional<Like> findByPostAndUser(Post post, User user);
}
