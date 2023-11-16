package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;
    private User testUser;

    @BeforeEach
    void setUp() {
        userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(User.builder()
                .email("mail@mail.ru")
                .login("dolore")
                .name("Nick Name")
                .birthday(LocalDate.parse("1946-08-20"))
                .build());
        userStorage.create(User.builder()
                .email("friend@mail.ru")
                .login("friend")
                .name("friend adipisicing")
                .birthday(LocalDate.parse("1976-08-20"))
                .build());
        userStorage.create(User.builder()
                .email("friend@common.ru")
                .login("common")
                .name("")
                .birthday(LocalDate.parse("2000-08-20"))
                .build());
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(2, 3);

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
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testUpdateWithNullableUser() {
    }

    @Test
    void testUpdateWithNullableId() {
    }

    @Test
    void testUpdateWhenIdIsUnknown() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testDeleteWithNullableUser() {
    }

    @Test
    void testDeleteWithNullableId() {
    }

    @Test
    void testDeleteWhenIdIsUnknown() {
    }

    @Test
    void testFindAll() {
    }

    @Test
    void testFindById() {
    }

    @Test
    void testFindByIdWhenIdIsUnknown() {
    }

    @Test
    void testFindByIdWithNullableId() {
    }

    @Test
    void testAddFriend() {
    }

    @Test
    void testAddFriendWhenUserIdIsUnknown() {
    }

    @Test
    void testAddFriendWhenFriendIdIsUnknown() {
    }

    @Test
    void testDeleteFriend() {
    }

    @Test
    void testDeleteFriendWhenUserIdIsUnknown() {
    }

    @Test
    void testDeleteFriendWhenFriendIdIsUnknown() {
    }

    @Test
    void testFindUserFriends() {
    }

    @Test
    void testFindUserFriendsWhenUserIdIsUnknown() {
    }

    @Test
    void testFindCommonFriends() {
    }

    @Test
    void testFindCommonFriendsWhenUserIdIsUnknown() {
    }

    @Test
    void testFindCommonFriendsWhenFriendIdIsUnknown() {
    }
}