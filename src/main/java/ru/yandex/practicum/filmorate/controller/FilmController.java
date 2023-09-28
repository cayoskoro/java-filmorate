package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            if (!isValidFilm(film)) {
                throw new ValidationException();
            }
            generateId();
            film.setId(getIdCounter());
            films.put(getIdCounter(), film);
            return film;
        } catch (ValidationException e) {
            return null;
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        try {
            if (!isValidFilm(film)) {
                throw new ValidationException();
            }

            if (Objects.isNull(films.get(film.getId()))) {
                return null;
            }

            films.put(film.getId(), film);
            return film;
        } catch (ValidationException e) {
            return null;
        }
    }

    @GetMapping
    public Map<Integer, Film> findAll() {
        return films;
    }

    private boolean isValidFilm(Film film) {
        return !Objects.isNull(film.getName()) && !Objects.isNull(film.getDuration())
                && !film.getName().isBlank()
                && film.getDescription().length() <= 200
                && !film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }

    private Integer generateId() {
        return ++idCounter;
    }

    private Integer getIdCounter() {
        return idCounter;
    }

}
