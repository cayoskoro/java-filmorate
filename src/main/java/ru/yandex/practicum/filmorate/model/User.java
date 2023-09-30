package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name == null || name.isBlank() ? login : name;
        this.birthday = birthday;
    }
}
