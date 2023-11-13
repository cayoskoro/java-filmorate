package ru.yandex.practicum.filmorate.storage.film;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Data
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> filmToMap = mapper.convertValue(film, new TypeReference<Map<String, Object>>() {});
        Integer createdFilmId = simpleJdbcInsert.executeAndReturnKey(filmToMap).intValue();
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
        return film;
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
        String sql = "SELECT * FROM films WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
        if (film == null) {
            log.info("Film by id = {} Not Found", id);
            throw new NotFoundException("Film Not Found");
        }
        return film;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
//                .mpa(rs.getInt("mpa_id"))
                .build();
    }
}
