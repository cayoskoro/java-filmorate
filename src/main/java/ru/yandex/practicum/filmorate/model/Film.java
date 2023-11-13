package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.EqualOrAfterSystemReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
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
    private final Set<Integer> likes;
    private final Set<String> genres;
    private final Mpa mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Set<Integer> likes, Set<String> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes == null ? new HashSet<>() : likes;
        this.genres = genres == null ? new HashSet<>() : genres;
        this.mpa = mpa;
    }
}
