package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Integer createdUserId = simpleJdbcInsert.executeAndReturnKey(userToFilm(user)).intValue();
        return findById(createdUserId);
    }

    @Override
    public User update(User user) {
        if (findById(user.getId()) == null) {
            log.info("{} Not Found", user);
            throw new NotFoundException("User Not Found");
        }
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return findById(user.getId());
    }

    @Override
    public User delete(User user) {
        if (findById(user.getId()) == null) {
            log.info("{} Not Found", user);
            throw new NotFoundException("User Not Found");
        }
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findById(Integer id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            log.info("User by id = {} Not Found", id);
            throw new NotFoundException("User Not Found");
        }
    }

    public void addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> findUserFriends(Integer userId) {
        String sql = "SELECT u.* FROM users u RIGHT JOIN friends f ON u.id = f.friend_id " +
                "WHERE user_id = ? ORDER BY u.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        String sql = "SELECT u.* FROM friends f1 " +
                "INNER JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "LEFT JOIN users u ON u.id = f1.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ? " +
                "ORDER BY u.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherUserId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private Map<String, String> userToFilm(User user) {
        return new HashMap<>() {{
            put("email", user.getEmail());
            put("login", user.getLogin());
            put("name", user.getName());
            put("birthday", String.valueOf(user.getBirthday()));
        }};
    }
}
