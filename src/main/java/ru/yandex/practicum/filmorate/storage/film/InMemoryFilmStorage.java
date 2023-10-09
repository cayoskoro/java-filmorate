package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public Film create(Film film) {
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

    @Override
    public Film update(Film film) {
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

    @Override
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
