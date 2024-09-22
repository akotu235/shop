package io.github.akotu235.shop.service.shop.projection.read;

import io.github.akotu235.shop.service.shop.entity.OrderStatus;

import java.util.List;

public class CartReadModel {
    private Long id;
    private Long userId;
    private List<CartPositionReadModel> positions;
    private OrderStatus status;
    private String cartPrice;
    private String currency;
    private int size;
    private boolean hasErrors;

    public CartReadModel(Long id, Long userId, List<CartPositionReadModel> positions, OrderStatus status, String cartPrice, String currency, int size, boolean hasErrors) {
        this.id = id;
        this.userId = userId;
        this.positions = positions;
        this.status = status;
        this.cartPrice = cartPrice;
        this.currency = currency;
        this.size = size;
        this.hasErrors = hasErrors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartPositionReadModel> getPositions() {
        return positions;
    }

    public void setPositions(List<CartPositionReadModel> positions) {
        this.positions = positions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
