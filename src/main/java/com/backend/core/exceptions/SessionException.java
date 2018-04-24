package com.backend.core.exceptions;

public class SessionException extends Exception {
    /**
     * Constructs a {@code HttpMethodException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SessionException(String msg) {
        super(msg);
    }
}

