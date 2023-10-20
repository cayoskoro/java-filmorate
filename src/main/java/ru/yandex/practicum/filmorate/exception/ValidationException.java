package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    private String property;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String property) {
        super(message + ":" + property);
    }
}
