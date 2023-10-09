package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public User create(User user) {
        checkPresentOrThrow(user);
        if (!isValidUser(user)) {
            throw new ValidationException();
        }

        User createdUser = user.toBuilder()
                .id(generateId())
                .build();
        users.put(createdUser.getId(), createdUser);
        return createdUser;
    }

    @Override
    public User update(User user) {
        checkPresentOrThrow(user);
        if (!isValidUser(user)) {
            throw new ValidationException();
        }

        if (Objects.isNull(users.get(user.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Integer generateId() {
        return ++idCounter;
    }

    private boolean isValidUser(User user) {
        return !Objects.isNull(user.getEmail()) && !Objects.isNull(user.getLogin())
                && !user.getEmail().isBlank() && user.getEmail().contains("@")
                && !user.getLogin().isBlank() && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now());
    }

    private void checkPresentOrThrow(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is null");
        }
    }
}
