package org.example.library.common;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}