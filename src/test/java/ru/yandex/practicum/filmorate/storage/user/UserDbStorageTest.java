package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;
    private User test1;
    private User test2;
    private User test3;
    private User testUser;

    @BeforeEach
    void setUp() {
        userStorage = new UserDbStorage(jdbcTemplate);
        test1 = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.parse("1946-08-20"))
                .build();
        test2 = User.builder()
                .id(2)
                .email("friend@mail.ru")
                .login("friend")
                .name("friend adipisicing")
                .birthday(LocalDate.parse("1976-08-20"))
                .build();
        test3 = User.builder()
                .id(3)
                .email("friend@common.ru")
                .login("common")
                .name("")
                .birthday(LocalDate.parse("2000-08-20"))
                .build();
        userStorage.create(test1);
        userStorage.create(test2);
        userStorage.create(test3);
        userStorage.addFriend(test1.getId(), test2.getId());
        userStorage.addFriend(test1.getId(), test3.getId());
        userStorage.addFriend(test2.getId(), test3.getId());

        testUser = User.builder()
                .id(4)
                .email("new@ya.ru")
                .login("new")
                .name("")
                .birthday(LocalDate.parse("2015-08-20"))
                .build();
    }

    @Test
    void testCreate() {
        assertThat(userStorage.create(testUser))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userStorage.findById(4));
    }

    @Test
    void testCreateWithNullableUser() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> userStorage.create(null)
        );
    }

    @Test
    void testUpdate() {
        userStorage.create(testUser);
        testUser.toBuilder()
                .name("mynewuser")
                .build();
        assertThat(userStorage.update(testUser))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userStorage.findById(4));
    }

    @Test
    void testUpdateWithNullableUser() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.update(null)
        );
    }

    @Test
    void testUpdateWithNullableId() {
        testUser.toBuilder()
                .id(null)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.update(testUser)
        );
    }

    @Test
    void testUpdateWhenIdIsUnknown() {
        testUser.toBuilder()
                .id(9999)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.update(testUser)
        );
    }

    @Test
    void testDelete() {
        userStorage.create(testUser);
        userStorage.delete(testUser);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.findById(4)
        );

    }

    @Test
    void testDeleteWithNullableUser() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.delete(null)
        );
    }

    @Test
    void testDeleteWithNullableId() {
        testUser.toBuilder()
                .id(null)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.delete(testUser)
        );
    }

    @Test
    void testDeleteWhenIdIsUnknown() {
        testUser.toBuilder()
                .id(9999)
                .build();
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.delete(testUser)
        );
    }

    @Test
    void testFindAll() {
        assertThat(userStorage.findAll())
                .isNotNull();
    }

    @Test
    void testFindById() {
        assertThat(userStorage.findById(test2.getId()))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(test2);
    }

    @Test
    void testFindByIdWhenIdIsUnknown() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.findById(9999)
        );
    }

    @Test
    void testFindByIdWithNullableId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.findById(null)
        );
    }

    @Test
    void testAddFriend() {
        userStorage.create(testUser);
        userStorage.addFriend(testUser.getId(), test2.getId());
        userStorage.addFriend(testUser.getId(), test1.getId());

        assertEquals(2, userStorage.findUserFriends(testUser.getId()).size(),
                "Количество друзей не сходится");
        assertIterableEquals(List.of(test1, test2),
                userStorage.findUserFriends(testUser.getId()),
                "Список запрошенных друзей не сходится");

    }

    @Test
    void testAddFriendWhenUserIdIsUnknown() {
        final DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userStorage.addFriend(9999, test2.getId())
        );
    }

    @Test
    void testAddFriendWhenFriendIdIsUnknown() {
        final DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userStorage.addFriend(test1.getId(), 9999)
        );
    }

    @Test
    void testDeleteFriend() {
        userStorage.create(testUser);
        userStorage.addFriend(test1.getId(), testUser.getId());
        userStorage.deleteFriend(test1.getId(), testUser.getId());

        assertEquals(2, userStorage.findUserFriends(test1.getId()).size(),
                "Количество друзей не сходится");
        assertIterableEquals(List.of(test2, test3),
                userStorage.findUserFriends(test1.getId()),
                "Список запрошенных друзей не сходится");
    }

    @Test
    void testDeleteFriendWhenUserIdIsUnknown() {
        assertDoesNotThrow(() -> userStorage.deleteFriend(9999, test2.getId()));
    }

    @Test
    void testDeleteFriendWhenFriendIdIsUnknown() {
        assertDoesNotThrow(() -> userStorage.deleteFriend(test1.getId(), 9999));
    }

    @Test
    void testFindUserFriends() {
        assertEquals(2, userStorage.findUserFriends(test1.getId()).size(),
                "Количество друзей не сходится");
        assertIterableEquals(List.of(test2, test3),
                userStorage.findUserFriends(test1.getId()),
                "Список запрошенных друзей не сходится");
    }

    @Test
    void testFindUserFriendsWhenUserIdIsUnknown() {
        assertTrue(userStorage.findUserFriends(9999).isEmpty(),
                "Список друзей несуществующего пользователя не пуст");
    }

    @Test
    void testFindCommonFriends() {
        assertEquals(1, userStorage.findCommonFriends(test1.getId(), test2.getId()).size(),
                "Количество общих друзей не сходится");
        assertIterableEquals(List.of(test3),
                userStorage.findCommonFriends(test1.getId(), test2.getId()),
                "Список запрошенных общих друзей не сходится");
    }

    @Test
    void testFindCommonFriendsWhenUserIdIsUnknown() {
        assertTrue(userStorage.findCommonFriends(9999, test2.getId()).isEmpty(),
                "Список общих друзей несуществующего пользователя не пуст");
    }

    @Test
    void testFindCommonFriendsWhenFriendIdIsUnknown() {
        assertTrue(userStorage.findCommonFriends(test1.getId(), 9999).isEmpty(),
                "Список общих друзей несуществующего пользователя не пуст");
    }
}