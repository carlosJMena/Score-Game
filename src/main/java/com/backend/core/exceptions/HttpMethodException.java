package com.backend.core.exceptions;

public class HttpMethodException extends Exception {
    /**
     * Constructs a {@code HttpMethodException} with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public HttpMethodException(String msg) {
        super(msg);
    }
}

