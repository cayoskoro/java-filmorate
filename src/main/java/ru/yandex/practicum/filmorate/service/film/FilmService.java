package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public Film addLike(Film film);
    public Film deleteLike(Film film);
    public List<Film> findTopTenByLikes();
}
