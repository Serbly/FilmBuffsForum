package org.example.filmbuffsforum.content.controller;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.content.model.movies.Movie;
import org.example.filmbuffsforum.content.service.MovieService;
import org.example.filmbuffsforum.content.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/movies")
public class MovieController {

    private final MovieService movieService;
    private final PostService postService;

    @GetMapping("/{id}")
    public String moviePage(@PathVariable Long id, Model model) {
        Movie movie = movieService.getById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("posts", postService.findByMovie(movie));
        return "content/movie";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "content/create-movie";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Movie movie) {
        movieService.save(movie);
        return "redirect:/";
    }
}
