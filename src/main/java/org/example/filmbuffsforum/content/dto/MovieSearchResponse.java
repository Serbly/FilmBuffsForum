package org.example.filmbuffsforum.content.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieSearchResponse {
    private Long id;
    private String title;
    private Integer releaseYear;
}
