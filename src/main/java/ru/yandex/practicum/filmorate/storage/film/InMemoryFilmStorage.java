package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films;
    private Integer idCounter = 0;

    @Override
    public Film create(Film film) {
        Film createdFilm = film.toBuilder()
                .id(generateId())
                .build();
        films.put(createdFilm.getId(), createdFilm);
        return createdFilm;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            log.info("{} Not Found", film);
            throw new NotFoundException("Film Not Found");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        if (films.get(film.getId()) == null) {
            log.info("{} Not Found", film);
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
        Film film = films.get(id);
        if (film == null) {
            log.info("Film by id = {} Not Found", id);
            throw new NotFoundException("Film Not Found");
        }
        return film;
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
