package org.example.filmbuffsforum.content.dto;

import lombok.Builder;
import lombok.Data;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Comment;
import org.example.filmbuffsforum.content.model.forum.Like;
import org.example.filmbuffsforum.content.model.movies.Movie;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    private User author;
    private LocalDateTime createdAt;
    private Movie movie;

    private List<Like> likes;
    private List<Comment> comments;
}
