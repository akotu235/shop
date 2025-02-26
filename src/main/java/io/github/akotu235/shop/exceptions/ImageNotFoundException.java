package io.github.akotu235.shop.exceptions;

public class ImageNotFoundException extends AppException {
    public ImageNotFoundException(String messageCode, String... args) {
        super(messageCode, args);
    }
}