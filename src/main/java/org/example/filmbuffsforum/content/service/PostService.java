package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.dto.CreatePostRequest;
import org.example.filmbuffsforum.content.dto.PostResponse;
import org.example.filmbuffsforum.content.model.forum.Post;
import org.example.filmbuffsforum.content.model.movies.Movie;
import org.example.filmbuffsforum.content.repository.forum.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MovieService movieService;
    private final FeedService feedService;

    public void create(CreatePostRequest request, User author) {

        Movie movie = null;

        if (request.getMovieId() != null) {
            movie = movieService.getById(request.getMovieId());
        }

        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(request.getTitle());
        post.setMovie(movie);
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
    }

    public List<Post> findByMovie(Movie movie) {
        return postRepository.findByMovie(movie);
    }

    public List<PostResponse> findByAuthor(User user) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user)
                .stream()
                .map(feedService::map)
                .toList();
    }
}
