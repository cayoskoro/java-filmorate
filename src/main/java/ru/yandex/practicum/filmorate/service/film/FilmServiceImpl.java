package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

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
            throw new ValidationException("Film is null");
        }

        StringBuilder invalidProperties = new StringBuilder();
        if (Objects.isNull(film.getDuration()) || film.getDuration() <= 0) {
            invalidProperties.append("duration");
        }
        if (Objects.isNull(film.getName()) || film.getName().isBlank()) {
            invalidProperties.append(", name");
        }
        if (film.getDescription().length() > 200) {
            invalidProperties.append(", description");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            invalidProperties.append(", releaseDate");
        }

        if (invalidProperties.length() != 0) {
            throw new ValidationException("Invalid Film property", invalidProperties.toString());
        }
    }
}
