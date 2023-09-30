package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
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
}
