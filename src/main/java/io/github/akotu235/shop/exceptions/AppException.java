package io.github.akotu235.shop.exceptions;

public class AppException extends RuntimeException {
    private final String[] args;

    public AppException(String messageCode, String... args) {
        super(messageCode);
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}