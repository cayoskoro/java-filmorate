package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoImpl;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoImpl;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmController filmController;
    private Film film;
    private Film updatedFilm;
    private static final String DESCRIPTION_EXAMPLE = "Пятеро друзей ( комик-группа «Шарло»), " +
            "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, " +
            "который задолжал им деньги, а именно 20 миллионов. " +
            "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.";

    @BeforeEach
    void setUp() {
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        MpaDao mpaDao = new MpaDaoImpl(jdbcTemplate);
        GenreDao genreDao = new GenreDaoImpl(jdbcTemplate);
        FilmServiceImpl filmService = new FilmServiceImpl(filmStorage, userStorage, mpaDao, genreDao);
        filmController = new FilmController(filmService);
        film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .mpa(mpaDao.findById(1).toBuilder().name(null).build())
                .build();
        updatedFilm = Film.builder()
                .id(1)
                .name("twin peaks")
                .description("mystery")
                .releaseDate(LocalDate.of(1990, 4, 8))
                .duration(100500)
                .mpa(mpaDao.findById(3).toBuilder().name(null).build())
                .build();
    }

    @Test
    void testCreate() {
        film = filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenRequestBodyIsEmpty() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(null)
        );
    }

    @Test
    void testCreateWhenFilmWithNullableName() {
        film = film.toBuilder().name(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithBlankName() {
        film = film.toBuilder().name("").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithDescriptionSizeEqual200() {
        film = filmController.create(film.toBuilder()
                .description(DESCRIPTION_EXAMPLE.substring(0, 200))
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenFilmWithDescriptionSizeMore200() {
        film = film.toBuilder().description(DESCRIPTION_EXAMPLE).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithReleaseDateEqualSystemDate() {
        film = filmController.create(film.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenFilmWithReleaseDateBeforeSystemDate() {
        film = film.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithNullableDuration() {
        film = film.toBuilder().duration(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithZeroDuration() {
        film = film.toBuilder().duration(0).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testCreateWhenFilmWithNegativeDuration() {
        film = film.toBuilder().duration(-1).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdate() {
        film = filmController.create(film);
        film = filmController.update(updatedFilm);
        assertTrue(filmController.findAll().contains(film), "Обновляемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenFilmWithoutId() {
        filmController.create(film);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmController.update(film)
        );
    }

    @Test
    void testUpdateWhenFilmNotFound() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmController.update(updatedFilm)
        );
    }

    @Test
    void testUpdateWhenRequestBodyIsEmpty() {
        filmController.create(film);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.update(null)
        );
    }

    @Test
    void testUpdateWhenFilmWithNullableName() {
        filmController.create(film);
        film = updatedFilm.toBuilder().name(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithBlankName() {
        filmController.create(film);
        film = updatedFilm.toBuilder().name("").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithDescriptionSizeEqual200() {
        filmController.create(film);
        film = filmController.create(updatedFilm.toBuilder()
                .description(DESCRIPTION_EXAMPLE.substring(0, 200))
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenFilmWithDescriptionSizeMore200() {
        filmController.create(film);
        film = updatedFilm.toBuilder().description(DESCRIPTION_EXAMPLE).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithReleaseDateEqualSystemDate() {
        filmController.create(film);
        film = filmController.create(updatedFilm.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenFilmWithReleaseDateBeforeSystemDate() {
        filmController.create(film);
        film = updatedFilm.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithNullableDuration() {
        filmController.create(film);
        film = updatedFilm.toBuilder().duration(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithZeroDuration() {
        filmController.create(film);
        film = updatedFilm.toBuilder().duration(0).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }

    @Test
    void testUpdateWhenFilmWithNegativeDuration() {
        filmController.create(film);
        film = updatedFilm.toBuilder().duration(-1).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film)
        );
    }
}