package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.content.dto.MovieSearchResponse;
import org.example.filmbuffsforum.content.model.movies.Movie;
import org.example.filmbuffsforum.content.repository.movies.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public List<MovieSearchResponse> search(String query) {
        return movieRepository
                .findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(query)
                .stream()
                .map(movie -> new MovieSearchResponse(movie.getId(), movie.getTitle(), movie.getReleaseYear()))
                .toList();
    }

    @Transactional
    public void save(Movie movie) {
        movieRepository.save(movie);
    }
}
