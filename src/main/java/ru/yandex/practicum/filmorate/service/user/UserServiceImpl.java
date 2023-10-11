package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        Set<Integer> updatedFriendSet = user.getFriends();
        updatedFriendSet.add(friendId);

        User updatedFilm = user.toBuilder()
                .friends(updatedFriendSet)
                .build();
        return userStorage.update(updatedFilm);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        Set<Integer> updatedFriendSet = user.getFriends();
        updatedFriendSet.remove(friendId);

        User updatedFilm = user.toBuilder()
                .friends(updatedFriendSet)
                .build();
        return userStorage.update(updatedFilm);
    }

    @Override
    public List<User> findUserFriends(Integer userId) {
        return userStorage.findById(userId).getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        Set<Integer> commonFriendIds = userStorage.findById(userId).getFriends();
        commonFriendIds.addAll(userStorage.findById(otherUserId).getFriends());
        return commonFriendIds.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
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
            throw new ValidationException("User is null");
        }

        StringBuilder invalidProperties = new StringBuilder();
        if (Objects.isNull(user.getEmail()) || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            invalidProperties.append("email");
        }
        if (Objects.isNull(user.getLogin()) || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            invalidProperties.append(", login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            invalidProperties.append(", birthday");
        }

        if (invalidProperties.length() != 0) {
            throw new ValidationException("Invalid User property", invalidProperties.toString());
        }
    }
}
