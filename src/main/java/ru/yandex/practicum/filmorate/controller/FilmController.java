package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(film.values());
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return null;
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return null;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable(name = "id") Integer filmId,
                        @PathVariable(name = "userId") Integer userId) {
        return null;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable(name = "id") Integer filmId,
                           @PathVariable(name = "userId") Integer userId) {
        return null;
    }
}
