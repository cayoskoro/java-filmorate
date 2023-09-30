package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    Film film;
    Film updatedFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        updatedFilm = Film.builder()
                .id(1)
                .name("twin peaks")
                .description("mystery")
                .releaseDate(LocalDate.of(1990, 4, 8))
                .duration(100500)
                .build();
    }

    @Test
    void testCreate() {
        film = filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenRequestBodyIsEmpty() {
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
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
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. Куглова, который за время была.")
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenFilmWithDescriptionSizeMore200() {
        film = film.toBuilder().description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                "Здесь они хотят разыскать господина Огюста Куглова, " +
                "который задолжал им деньги, а именно 20 миллионов. " +
                "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();
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
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> filmController.update(film)
        );
    }

    @Test
    void testUpdateWhenFilmNotFound() {
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> filmController.update(updatedFilm)
        );
    }

    @Test
    void testUpdateWhenRequestBodyIsEmpty() {
        filmController.create(film);
        final ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
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
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. Куглова, который за время была.")
                .build());
        assertTrue(filmController.findAll().contains(film), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenFilmWithDescriptionSizeMore200() {
        filmController.create(film);
        film = updatedFilm.toBuilder().description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. " +
                        "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();
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