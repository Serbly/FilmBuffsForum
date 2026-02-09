package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.content.dto.MovieSearchResponse;
import org.example.filmbuffsforum.content.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;

    @GetMapping("/search")
    public List<MovieSearchResponse> search(@RequestParam String q) {
        return movieService.search(q);
    }
}
