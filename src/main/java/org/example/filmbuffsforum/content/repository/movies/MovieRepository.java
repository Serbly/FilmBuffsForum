package org.example.filmbuffsforum.content.repository.movies;

import org.example.filmbuffsforum.content.model.movies.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(String title);
}
