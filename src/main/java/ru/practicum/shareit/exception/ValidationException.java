package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
        log.error(message);
    }
}
