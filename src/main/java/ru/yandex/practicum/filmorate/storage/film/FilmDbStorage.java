package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Integer createdFilmId = simpleJdbcInsert.executeAndReturnKey(filmToMap(film)).intValue();
        return findById(createdFilmId);
    }

    @Override
    public Film update(Film film) {
        if (findById(film.getId()) == null) {
            log.info("{} Not Found", film);
            throw new NotFoundException("Film Not Found");
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return findById(film.getId());
    }

    @Override
    public Film delete(Film film) {
        if (findById(film.getId()) == null) {
            log.info("{} Not Found", film);
            throw new NotFoundException("Film Not Found");
        }
        String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, film.getId());
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findById(Integer id) {
        try {
            String sql = "SELECT * FROM films WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Film by id = {} Not Found", id);
            throw new NotFoundException("Film Not Found");
        }
    }

    public List<Film> findPopularFilms(Integer count) {
        String sql = "SELECT f.* " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder().id(rs.getInt("mpa_id")).build())
                .build();
    }

    private Map<String, String> filmToMap(Film film) {
        return new HashMap<>() {{
            put("name", film.getName());
            put("description", film.getDescription());
            put("release_date", String.valueOf(film.getReleaseDate()));
            put("duration", String.valueOf(film.getDuration()));
            put("mpa_id", String.valueOf(film.getMpa().getId()));
        }};
    }
}
