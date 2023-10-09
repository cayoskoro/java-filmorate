package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addLike(Film film) {
        return null;
    }

    @Override
    public Film deleteLike(Film film) {
        return null;
    }

    @Override
    public List<Film> findTopTenByLikes() {
        return null;
    }
}
