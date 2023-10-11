package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private Integer idCounter = 0;

    @Autowired
    public InMemoryFilmStorage(Map<Integer, Film> films) {
        this.films = films;
    }

    @Override
    public Film create(Film film) {
        Film createdFilm = film.toBuilder()
                .id(generateId())
                .build();
        return films.put(createdFilm.getId(), createdFilm);
    }

    @Override
    public Film update(Film film) {
        if (Objects.isNull(films.get(film.getId()))) {
            throw new NotFoundException("Film Not Found");
        }
        return films.put(film.getId(), film);
    }

    @Override
    public Film delete(Film film) {
        if (Objects.isNull(films.get(film.getId()))) {
            throw new NotFoundException("Film Not Found");
        }
        return films.remove(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Integer id) {
        return films.get(id);
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
