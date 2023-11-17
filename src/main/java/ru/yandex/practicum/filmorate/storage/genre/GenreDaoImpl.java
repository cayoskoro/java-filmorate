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
import java.util.ArrayList;
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
        List<Genre> unknownGenres = new ArrayList<>(genres);
        List<Genre> existingGenres = findAll();
        unknownGenres.removeAll(findAll());
        if (!unknownGenres.isEmpty()) {
            log.debug("Input genres: {}; Existing genres: {}", genres, existingGenres);
            log.info("Genres {} Not Found", unknownGenres);
            throw new NotFoundException("Genres Not Found");
        }

        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (Genre genre : genres) {
            batchArgs.add(new Object[]{filmId, genre.getId()});
        }

        deleteFilmGenres(filmId);
        jdbcTemplate.batchUpdate(sql, batchArgs);
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
