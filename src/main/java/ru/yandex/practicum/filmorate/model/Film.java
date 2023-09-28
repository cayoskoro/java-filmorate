package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.EqualOrAfterSystemReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @EqualOrAfterSystemReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}
