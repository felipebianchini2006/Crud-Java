package org.example.library.common;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s com identificador %s não encontrado", resource, id));
    }
}