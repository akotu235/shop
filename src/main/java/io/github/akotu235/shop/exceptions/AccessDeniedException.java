package io.github.akotu235.shop.exceptions;

public class AccessDeniedException extends AppException {
    public AccessDeniedException(String messageCode, String... args) {
        super(messageCode, args);
    }
}