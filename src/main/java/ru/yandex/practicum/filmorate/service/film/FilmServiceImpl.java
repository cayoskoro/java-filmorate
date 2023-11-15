package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        userStorage.findById(filmId);
        userStorage.findById(userId);
        filmStorage.addLike(filmId, userId);
        return findById(filmId);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        userStorage.findById(filmId);
        userStorage.findById(userId);
        filmStorage.deleteLike(filmId, userId);
        return findById(filmId);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.findPopularFilms(count).stream()
                .map(it -> it.toBuilder()
                        .genres(new HashSet<>(genreDao.findFilmGenres(it.getId())))
                        .mpa(mpaDao.findById(it.getMpa().getId()))
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public Film create(Film film) {
        checkValidOrThrow(film);
        Film newFilm = filmStorage.create(film);
        Mpa mpa = mpaDao.findById(newFilm.getMpa().getId());
        genreDao.addFilmGenres(newFilm.getId(), new ArrayList<>(film.getGenres()));
        Set<Genre> genres = new HashSet<>(genreDao.findFilmGenres(newFilm.getId()));
        return newFilm.toBuilder()
                .genres(genres)
                .mpa(mpa)
                .build();
    }

    @Override
    public Film update(Film film) {
        checkValidOrThrow(film);
        Film updatedFilm = filmStorage.update(film);
        Mpa mpa = mpaDao.findById(updatedFilm.getMpa().getId());
        genreDao.addFilmGenres(updatedFilm.getId(), new ArrayList<>(film.getGenres()));
        Set<Genre> genres = new HashSet<>(genreDao.findFilmGenres(updatedFilm.getId()));
        return updatedFilm.toBuilder()
                .genres(genres)
                .mpa(mpa)
                .build();
    }

    @Override
    public Film delete(Film film) {
        checkValidOrThrow(film);
        return filmStorage.delete(film);
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll().stream()
                .map(it -> it.toBuilder()
                        .genres(new HashSet<>(genreDao.findFilmGenres(it.getId())))
                        .mpa(mpaDao.findById(it.getMpa().getId()))
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public Film findById(Integer id) {
        Film film = filmStorage.findById(id);
        Mpa mpa = mpaDao.findById(film.getMpa().getId());
        Set<Genre> genres = new HashSet<>(genreDao.findFilmGenres(film.getId()));
        return film.toBuilder()
                .genres(genres)
                .mpa(mpa)
                .build();
    }

    private void checkValidOrThrow(Film film) {
        if (film == null) {
            log.info("Film is null");
            throw new ValidationException("Film is null");
        }

        List<String> invalidProperties = new ArrayList<>();
        if (film.getDuration() == null || film.getDuration() <= 0) {
            invalidProperties.add("duration");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            invalidProperties.add("name");
        }
        if (film.getDescription().length() > 200) {
            invalidProperties.add("description");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            invalidProperties.add("releaseDate");
        }

        if (!invalidProperties.isEmpty()) {
            log.info("Invalid {} property: {}", film, invalidProperties);
            throw new ValidationException("Invalid Film property", invalidProperties.toString());
        }
    }
}
