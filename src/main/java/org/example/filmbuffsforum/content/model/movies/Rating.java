package org.example.filmbuffsforum.content.model.movies;

import jakarta.persistence.*;
import lombok.Data;
import org.example.filmbuffsforum.auth.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@IdClass(RatingId.class)
@Data
public class Rating {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private int rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
