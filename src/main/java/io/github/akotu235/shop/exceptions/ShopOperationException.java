package io.github.akotu235.shop.exceptions;

public class ShopOperationException extends AppException {
    public ShopOperationException(String messageCode, String... args) {
        super(messageCode, args);
    }
}