package org.example.filmbuffsforum.content.repository.movies;

import org.example.filmbuffsforum.content.model.movies.Rating;
import org.example.filmbuffsforum.content.model.movies.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
}
