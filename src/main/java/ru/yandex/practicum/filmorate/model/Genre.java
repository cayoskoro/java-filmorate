package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Genre {
    private final Integer id;
    @NotBlank
    private final String name;
}
