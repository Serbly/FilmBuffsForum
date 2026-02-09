package org.example.filmbuffsforum.content.repository.forum;

import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.example.filmbuffsforum.content.model.movies.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByMovie(Movie movie);

    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
}
