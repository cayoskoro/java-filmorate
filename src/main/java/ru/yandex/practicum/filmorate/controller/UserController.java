package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();
    private Integer idCounter = 0;

    @PostMapping
    public User create(@RequestBody User user) {
        try {
            if (!isValidUser(user)) {
                throw new ValidationException();
            }

            if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            generateId();
            user.setId(getIdCounter());
            users.add(user);
            return user;
        } catch (ValidationException e) {
            return null;
        }
    }

    @PutMapping
    public User update(@RequestBody User user) {
        try {
            if (!isValidUser(user)) {
                throw new ValidationException();
            }

            if (!users.contains(user)) {
                return null;
            }

            if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.add(user);
            return user;
        } catch (ValidationException e) {
            return null;
        }
    }

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    private boolean isValidUser(User user) {
        return !Objects.isNull(user.getEmail()) && !Objects.isNull(user.getLogin())
                && !user.getEmail().isBlank() && user.getEmail().contains("@")
                && !user.getLogin().isBlank() && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now());
    }

    private Integer generateId() {
        return ++idCounter;
    }

    private Integer getIdCounter() {
        return idCounter;
    }
}
