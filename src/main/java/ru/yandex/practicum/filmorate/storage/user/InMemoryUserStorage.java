package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private Integer idCounter = 0;

    @Autowired
    public InMemoryUserStorage(Map<Integer, User> users) {
        this.users = users;
    }

    @Override
    public User create(User user) {
        User createdUser = user.toBuilder()
                .id(generateId())
                .build();
        return users.put(createdUser.getId(), createdUser);
    }

    @Override
    public User update(User user) {
        if (Objects.isNull(users.get(user.getId()))) {
            throw new NotFoundException("Film Not Found");
        }
        return users.put(user.getId(), user);
    }

    @Override
    public User delete(User user) {
        if (Objects.isNull(users.get(user.getId()))) {
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
        return users.get(id);
    }

    private Integer generateId() {
        return ++idCounter;
    }
}
