package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private Integer idCounter = 0;

    @Override
    public User create(User user) {
        User createdUser = user.toBuilder()
                .id(generateId())
                .build();
        users.put(createdUser.getId(), createdUser);
        return createdUser;
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) == null) {
            log.info("{} Not Found", user);
            throw new NotFoundException("User Not Found");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(User user) {
        if (users.get(user.getId()) == null) {
            log.info("{} Not Found", user);
            throw new NotFoundException("User Not Found");
        }
        return users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Integer id) {
        User user = users.get(id);
        if (user == null) {
            log.info("User by id = {} Not Found", id);
            throw new NotFoundException("User Not Found");
        }
        return user;
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
