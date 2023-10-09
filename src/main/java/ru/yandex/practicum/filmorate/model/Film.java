package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.EqualOrAfterSystemReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private final Integer id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    private final String description;
    @EqualOrAfterSystemReleaseDate
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private final Set<Long> likes;
}
