package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public User addFriend(User user);
    public User deleteFriend(User user);
    public List<User> findCommonFriends();
}
