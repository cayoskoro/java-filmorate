package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre findById(Integer id) {
        try {
            String sql = "SELECT * FROM genres WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Genre by id = {} Not Found", id);
            throw new NotFoundException("Genre Not Found");
        }
    }

    @Override
    public List<Genre> findFilmGenres(Integer filmId) {
        String sql = "SELECT g.id, g.name " +
                "FROM film_genre fg " +
                "LEFT JOIN films f ON fg.film_id = f.id " +
                "RIGHT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE f.id = ? " +
                "ORDER BY g.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    @Override
    public void addFilmGenres(Integer filmId, List<Genre> genres) {
        deleteFilmGenres(filmId);
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            findById(genre.getId());
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    @Override
    public void deleteFilmGenres(Integer filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
