package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        checkPresentOrThrow(film);
        if (!isValidFilm(film)) {
            throw new ValidationException();
        }

        Film createdFilm = film.toBuilder()
                .id(generateId())
                .build();
        films.put(createdFilm.getId(), createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        checkPresentOrThrow(film);
        if (!isValidFilm(film)) {
            throw new ValidationException();
        }

        if (Objects.isNull(films.get(film.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film Not Found");
        }

        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    private Integer generateId() {
        return ++idCounter;
    }

    private boolean isValidFilm(Film film) {
        return !Objects.isNull(film.getName()) && !Objects.isNull(film.getDuration())
                && !film.getName().isBlank()
                && film.getDescription().length() <= 200
                && !film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }

    private void checkPresentOrThrow(Film film) {
        if (film == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Film is null");
        }
    }
}
