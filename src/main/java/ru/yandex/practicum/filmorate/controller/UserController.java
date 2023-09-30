package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (Objects.isNull(users.get(user.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Integer generateId() {
        return ++idCounter;
    }

    private User defineUserName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
