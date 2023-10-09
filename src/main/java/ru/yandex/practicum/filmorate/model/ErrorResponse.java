package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ErrorResponse {
    @NotBlank
    private final String description;
}
