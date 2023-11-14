package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        Set<Integer> updatedLikeSet = new HashSet<>(film.getLikes());
        updatedLikeSet.add(userStorage.findById(userId).getId());

        Film updatedFilm = film.toBuilder()
                .likes(updatedLikeSet)
                .build();
        return filmStorage.update(updatedFilm);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        Set<Integer> updatedLikeSet = new HashSet<>(film.getLikes());
        updatedLikeSet.remove(userStorage.findById(userId).getId());

        Film updatedFilm = film.toBuilder()
                .likes(updatedLikeSet)
                .build();
        return filmStorage.update(updatedFilm);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film create(Film film) {
        checkValidOrThrow(film);
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        checkValidOrThrow(film);
        return filmStorage.update(film);
    }

    @Override
    public Film delete(Film film) {
        checkValidOrThrow(film);
        return filmStorage.delete(film);
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film findById(Integer id) {
        return filmStorage.findById(id);
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
