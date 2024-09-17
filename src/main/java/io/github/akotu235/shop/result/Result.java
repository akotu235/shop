package io.github.akotu235.shop.result;

public record Result<T>(boolean isSuccess, T object, String messageCode, String... args) {
}