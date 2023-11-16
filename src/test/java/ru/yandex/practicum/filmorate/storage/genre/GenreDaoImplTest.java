package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDaoImplTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDao genreDao;
    private FilmDbStorage filmStorage;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genreDao = new GenreDaoImpl(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.parse("1967-03-25"))
                .duration(100)
                .mpa(Mpa.builder().id(1).build())
                .build());
        filmStorage.create(Film.builder()
                .name("New film")
                .description("New film about friends")
                .releaseDate(LocalDate.parse("1999-04-30"))
                .duration(120)
                .mpa(Mpa.builder().id(3).build())
                .build());
        genreDao.addFilmGenres(2, List.of(genreDao.findById(1), genreDao.findById(2), genreDao.findById(3)));
        genre = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
    }

    @Test
    void testFindAll() {
        assertThat(genreDao.findAll())
                .isNotNull();
    }

    @Test
    void testFindById() {
        assertThat(genreDao.findById(genre.getId()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @Test
    void testFindByIdWhenIdIsUnknown() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> genreDao.findById(9999)
        );
    }

    @Test
    void testFindByIdWithNullableId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> genreDao.findById(null)
        );
    }

    @Test
    void testFindFilmGenres() {
        assertThat(genreDao.findFilmGenres(2))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(List.of(genreDao.findById(1), genreDao.findById(2), genreDao.findById(3)));
    }

    @Test
    void testFindFilmGenresWhenIdIsUnknown() {
        assertTrue(genreDao.findFilmGenres(9999).isEmpty(),
                "Список жанров фильма с неизвестным id не пуст");
    }

    @Test
    void testFindFilmGenresWithNullableId() {
        assertTrue(genreDao.findFilmGenres(null).isEmpty(),
                "Список жанров фильма с неизвестным id не пуст");
    }

    @Test
    void testAddFilmGenres() {
        genreDao.addFilmGenres(2, List.of(genreDao.findById(2), genreDao.findById(5)));
        assertThat(genreDao.findFilmGenres(2))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(List.of(genreDao.findById(2), genreDao.findById(5)));
    }

    @Test
    void testDeleteFilmGenres() {
        genreDao.deleteFilmGenres(2);
        assertTrue(genreDao.findFilmGenres(2).isEmpty(), "Список жанров фильма после удаления не пуст");
    }

    @Test
    void testDeleteFilmGenresWhenIdIsUnknown() {
        genreDao.deleteFilmGenres(9999);
    }

    @Test
    void testDeleteFilmGenresWithNullableId() {
        genreDao.deleteFilmGenres(null);
    }
}