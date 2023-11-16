package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    public List<Genre> findAll();

    public Genre findById(Integer id);

    public List<Genre> findFilmGenres(Integer filmId);

    public void addFilmGenres(Integer filmId, List<Genre> genres);

    public void deleteFilmGenres(Integer filmId);
}
