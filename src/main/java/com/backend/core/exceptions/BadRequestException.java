package com.backend.core.exceptions;

public class BadRequestException extends Exception {
    /**
     * Constructs a {@code BadRequestException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public BadRequestException(String msg) {
        super(msg);
    }
}

