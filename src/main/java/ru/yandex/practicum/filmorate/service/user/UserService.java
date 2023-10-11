package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public User addFriend(Integer userId, Integer friendId);
    public User deleteFriend(Integer userId, Integer friendId);
    public List<User> findUserFriends(Integer userId);
    public List<User> findCommonFriends(Integer userId, Integer otherUserId);
    public User create(User user);
    public User update(User user);
    public User delete(User user);
    public List<User> findAll();
    public User findById(Integer id);
}
