package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @PostMapping
    public Film create(@Valid  @RequestBody Film film) {
        try {
            film.setId(generateId());
            films.put(film.getId(), film);
            return film;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Provide correct User fields", e);
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        try {
            if (Objects.isNull(films.get(film.getId()))) {
                throw new ValidationException();
            }

            films.put(film.getId(), film);
            return film;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Film Not Found", e);
        }
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
