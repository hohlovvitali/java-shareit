package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateException extends Exception {
    public DuplicateException(String m) {
        super(m);
        log.error(m);
    }
}
