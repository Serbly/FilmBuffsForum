package org.example.filmbuffsforum.content.repository.movies;

import org.example.filmbuffsforum.content.model.movies.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
