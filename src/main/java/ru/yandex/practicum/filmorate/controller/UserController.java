package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        return null;
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable Integer id) {
        return null;
    }

    @GetMapping("/{id}/friends/common/{otherId} ")
    public List<User> findCommonFriendsWithOtherUser(@PathVariable(name = "id") Integer userId,
                                                     @PathVariable(name = "otherId") Integer otherUserId) {
        return null;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(name = "id") Integer userId,
                     @PathVariable(name = "friendId") Integer friendId) {
        return userService.addFriend();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(name = "id") Integer userId,
                          @PathVariable(name = "friendId") Integer friendId) {
        return null;
    }
}
