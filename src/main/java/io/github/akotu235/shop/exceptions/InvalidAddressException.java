package io.github.akotu235.shop.exceptions;

public class InvalidAddressException extends AppException {
    public InvalidAddressException(String messageCode, String... args) {
        super(messageCode, args);
    }
}