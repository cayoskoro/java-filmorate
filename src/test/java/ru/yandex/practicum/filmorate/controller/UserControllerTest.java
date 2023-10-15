package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User user;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        UserStorage userStorage = new InMemoryUserStorage(new HashMap<>());
        UserServiceImpl userService = new UserServiceImpl(userStorage);
        userController = new UserController(userService);
        user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        updatedUser = User.builder()
                .id(1)
                .login("cooper")
                .name("Douglas Jones")
                .email("agent@yandex.ru")
                .birthday(LocalDate.of(1989, 1, 1))
                .build();
    }

    @Test
    void testCreate() {
        user = userController.create(user);
        assertTrue(userController.findAll().contains(user), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenRequestBodyIsEmpty() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(null)
        );
    }

    @Test
    void testCreateWhenUserWithFailedEmail() {
        user = user.toBuilder().email("mail.ru").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
    }

    @Test
    void testCreateWhenUserWithNullableLogin() {
        user = user.toBuilder().login(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
    }

    @Test
    void testCreateWhenUserWithBlankLogin() {
        user = user.toBuilder().login("").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
    }

    @Test
    void testCreateWhenUserWithSpacesInLogin() {
        user = user.toBuilder().login("Nick Login").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
    }

    @Test
    void testCreateWhenUserWithNullableName() {
        user = userController.create(user.toBuilder()
                .name(null)
                .build());
        assertTrue(userController.findAll().contains(user), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenUserWithBlankName() {
        user = userController.create(user.toBuilder()
                .name("")
                .build());
        assertTrue(userController.findAll().contains(user), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testCreateWhenUserWithBirthdayInFutureLocalDate() {
        user = user.toBuilder().birthday(LocalDate.of(2446, 8, 20)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user)
        );
    }

    @Test
    void testCreateWhenUserWithBirthdayIsEqualToNowLocalDate() {
        user = userController.create(user.toBuilder()
                .birthday(LocalDate.now())
                .build());
        assertTrue(userController.findAll().contains(user), "Создаваемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdate() {
        user = userController.create(user);
        user = userController.update(updatedUser);
        assertTrue(userController.findAll().contains(user), "Обновляемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenUserWithoutId() {
        userController.create(user);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserNotFound() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userController.update(updatedUser)
        );
    }

    @Test
    void testUpdateWhenRequestBodyIsEmpty() {
        userController.create(user);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(null)
        );
    }

    @Test
    void testUpdateWhenUserWithFailedEmail() {
        userController.create(user);
        user = updatedUser.toBuilder().email("mail.ru").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserWithNullableLogin() {
        userController.create(user);
        user = updatedUser.toBuilder().login(null).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserWithBlankLogin() {
        userController.create(user);
        user = updatedUser.toBuilder().login("").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserWithSpacesInLogin() {
        userController.create(user);
        user = updatedUser.toBuilder().login("Nick Login").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserWithNullableName() {
        userController.create(user);
        user = userController.update(updatedUser.toBuilder()
                .name(null)
                .build());
        assertTrue(userController.findAll().contains(user), "Обновляемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenUserWithBlankName() {
        userController.create(user);
        user = userController.update(updatedUser.toBuilder()
                .name("")
                .build());
        assertTrue(userController.findAll().contains(user), "Обновляемого юнита нет в списке юнитов");
    }

    @Test
    void testUpdateWhenUserWithBirthdayInFutureLocalDate() {
        userController.create(user);
        user = updatedUser.toBuilder().birthday(LocalDate.of(2446, 8, 20)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.update(user)
        );
    }

    @Test
    void testUpdateWhenUserWithBirthdayIsEqualToNowLocalDate() {
        userController.create(user);
        user = userController.update(updatedUser.toBuilder()
                .birthday(LocalDate.now())
                .build());
        assertTrue(userController.findAll().contains(user), "Обновляемого юнита нет в списке юнитов");

    }
}