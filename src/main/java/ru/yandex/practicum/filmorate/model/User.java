package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private final Integer id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private final String login;
    private final String name;
    @PastOrPresent
    private final LocalDate birthday;
    private final Set<Long> friends;

    public User(Integer id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name == null || name.isBlank() ? login : name;
        this.birthday = birthday;
        this.friends = friends;
    }
}
