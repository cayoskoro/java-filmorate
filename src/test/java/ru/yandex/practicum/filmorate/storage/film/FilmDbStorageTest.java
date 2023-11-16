package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoImpl;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmStorage;
    private Film test1;
    private Film test2;
    private Film testFilm;
    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        MpaDao mpaDao = new MpaDaoImpl(jdbcTemplate);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        test1 = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration(100)
                .mpa(mpaDao.findById(1).toBuilder().name(null).build())
                .build();
        test2 = Film.builder()
                .id(2)
                .name("New film")
                .description("New film about friends")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(100)
                .mpa(mpaDao.findById(3).toBuilder().name(null).build())
                .build();
        filmStorage.create(test1);
        filmStorage.create(test2);

        testUser1 = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.parse("1946-08-20"))
                .build();
        testUser2 = User.builder()
                .id(2)
                .email("friend@mail.ru")
                .login("friend")
                .name("friend adipisicing")
                .birthday(LocalDate.parse("1976-08-20"))
                .build();
        testUser3 = User.builder()
                .id(3)
                .email("friend@common.ru")
                .login("common")
                .name("")
                .birthday(LocalDate.parse("2000-08-20"))
                .build();
        userStorage.create(testUser1);
        userStorage.create(testUser2);
        userStorage.create(testUser3);
        filmStorage.addLike(test1.getId(), testUser1.getId());
        filmStorage.addLike(test1.getId(), testUser2.getId());
        filmStorage.addLike(test2.getId(), testUser3.getId());

        testFilm = Film.builder()
                .id(3)
                .name("My First Film")
                .description("What is this")
                .releaseDate(LocalDate.parse("1988-12-12"))
                .duration(50)
                .mpa(mpaDao.findById(5).toBuilder().name(null).build())
                .build();
    }

    @Test
    void testCreate() {
        assertThat(filmStorage.create(testFilm))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmStorage.findById(3));
    }

    @Test
    void testCreateWithNullableFilm() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> filmStorage.create(null)
        );
    }

    @Test
    void testUpdate() {
        filmStorage.create(testFilm);
        testFilm.toBuilder()
                .name("mynew")
                .build();
        assertThat(filmStorage.update(testFilm))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmStorage.findById(3));
    }

    @Test
    void testUpdateWithNullableFilm() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> filmStorage.update(null)
        );
    }

    @Test
    void testUpdateWithNullableId() {
        testFilm.toBuilder()
                .id(null)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.update(testFilm)
        );
    }

    @Test
    void testUpdateWhenIdIsUnknown() {
        testFilm.toBuilder()
                .id(9999)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.update(testFilm)
        );
    }

    @Test
    void testDelete() {
        filmStorage.create(testFilm);
        filmStorage.delete(testFilm);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.findById(3)
        );

    }

    @Test
    void testDeleteWithNullableFilm() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> filmStorage.delete(null)
        );
    }

    @Test
    void testDeleteWithNullableId() {
        testFilm.toBuilder()
                .id(null)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.delete(testFilm)
        );
    }

    @Test
    void testDeleteWhenIdIsUnknown() {
        testFilm.toBuilder()
                .id(9999)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.delete(testFilm)
        );
    }

    @Test
    void testFindAll() {
        assertThat(filmStorage.findAll())
                .isNotNull();
    }

    @Test
    void testFindById() {
        assertThat(filmStorage.findById(test2.getId()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(test2);
    }

    @Test
    void testFindByIdWhenIdIsUnknown() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.findById(9999)
        );
    }

    @Test
    void testFindByIdWithNullableId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.findById(null)
        );
    }

    @Test
    void testAddLike() {
        filmStorage.create(testFilm);
        filmStorage.addLike(testFilm.getId(), testUser3.getId());
        filmStorage.addLike(testFilm.getId(), testUser2.getId());
        filmStorage.addLike(testFilm.getId(), testUser1.getId());

        assertEquals(Optional.of(testFilm), filmStorage.findPopularFilms(10).stream().findFirst(),
                "Самый популярный фильм не сходится");
        assertIterableEquals(List.of(testFilm, test1, test2),
                filmStorage.findPopularFilms(10),
                "Порядок списка популярных фильмов не сходится");

    }

    @Test
    void testAddLikeWhenUserIdIsUnknown() {
        final DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> filmStorage.addLike(test2.getId(), 9999)
        );
    }

    @Test
    void testAddLikeWhenFilmIdIsUnknown() {
        final DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> filmStorage.addLike(9999, testUser3.getId())
        );
    }

    @Test
    void testDeleteLike() {
        filmStorage.create(testFilm);
        filmStorage.addLike(testFilm.getId(), testUser3.getId());
        filmStorage.addLike(testFilm.getId(), testUser2.getId());
        filmStorage.addLike(testFilm.getId(), testUser1.getId());
        filmStorage.deleteLike(testFilm.getId(), testUser2.getId());

        assertEquals(Optional.of(test1), filmStorage.findPopularFilms(10).stream().findFirst(),
                "Самый популярный фильм не сходится");
        assertIterableEquals(List.of(test1, testFilm, test2),
                filmStorage.findPopularFilms(10),
                "Порядок списка популярных фильмов не сходится");
    }

    @Test
    void testDeleteLikeWhenUserIdIsUnknown() {
        assertDoesNotThrow(() -> filmStorage.deleteLike(test2.getId(), 9999));
    }

    @Test
    void testDeleteLikeWhenFilmIdIsUnknown() {
        assertDoesNotThrow(() -> filmStorage.deleteLike(9999, testUser1.getId()));
    }

    @Test
    void findPopularFilms() {
        assertEquals(Optional.of(test1), filmStorage.findPopularFilms(10).stream().findFirst(),
                "Самый популярный фильм не сходится");
        assertIterableEquals(List.of(test1, test2),
                filmStorage.findPopularFilms(10),
                "Порядок списка популярных фильмов не сходится");
    }
}