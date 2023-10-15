package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public Film addLike(Integer filmId, Integer userId);

    public Film deleteLike(Integer filmId, Integer userId);

    public List<Film> findPopularFilms(Integer count);

    public Film create(Film film);

    public Film update(Film film);

    public Film delete(Film film);

    public List<Film> findAll();

    public Film findById(Integer id);
}
