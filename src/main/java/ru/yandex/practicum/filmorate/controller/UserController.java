package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idCounter = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            user.setId(generateId());
            users.put(user.getId(), user);
            return user;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Provide correct User fields", e);
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        try {
            if (Objects.isNull(users.get(user.getId()))) {
                throw new ValidationException();
            }

            if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            users.put(user.getId(), user);
            return user;
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User Not Found", e);
        }
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
