package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDbStorage userStorage;

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = findById(userId);
        findById(friendId);
        userStorage.addFriend(userId, friendId);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = findById(userId);
        findById(friendId);
        userStorage.deleteFriend(userId, friendId);
        return user;
    }

    @Override
    public List<User> findUserFriends(Integer userId) {
        findById(userId);
        return userStorage.findUserFriends(userId);
    }

    @Override
    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        findById(userId);
        return userStorage.findCommonFriends(userId, otherUserId);
    }

    @Override
    public User create(User user) {
        checkValidOrThrow(user);
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        checkValidOrThrow(user);
        return userStorage.update(user);
    }

    @Override
    public User delete(User user) {
        checkValidOrThrow(user);
        return userStorage.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userStorage.findById(id);
    }

    private void checkValidOrThrow(User user) {
        if (user == null) {
            log.info("User is null");
            throw new ValidationException("User is null");
        }

        List<String> invalidProperties = new ArrayList<>();
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            invalidProperties.add("email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            invalidProperties.add("login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            invalidProperties.add("birthday");
        }

        if (!invalidProperties.isEmpty()) {
            log.info("Invalid {} property: {}", user, invalidProperties);
            throw new ValidationException("Invalid User property", invalidProperties.toString());
        }
    }
}
