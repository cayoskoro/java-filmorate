package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmServiceImpl filmService;

    @Autowired
    public FilmController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return filmService.findById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.findPopularFilms(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable(name = "id") Integer filmId,
                        @PathVariable(name = "userId") Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable(name = "id") Integer filmId,
                           @PathVariable(name = "userId") Integer userId) {
        return filmService.deleteLike(filmId, userId);
    }
}
