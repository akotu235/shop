package io.github.akotu235.shop.exceptions;

public class FileOperationException extends AppException {
    public FileOperationException(String messageCode, String... args) {
        super(messageCode, args);
    }
}