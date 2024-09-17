package io.github.akotu235.shop.service.shop.projection.read;

import io.github.akotu235.shop.service.shop.entity.OrderStatus;

import java.util.List;

public class OrderReadModel {
    private Long id;
    private Long userId;
    private String recipientName;
    private List<OrderPositionReadModel> positions;
    private OrderStatus status;
    private DeliveryOptionReadModel deliveryOption;
    private ShippingDetailsReadModel shippingDetails;
    private String cartPrice;
    private String totalPrice;
    private String currency;
    private int size;
    private String submissionDate;

    public OrderReadModel(Long id, Long userId, String recipientName, List<OrderPositionReadModel> positions, OrderStatus status, DeliveryOptionReadModel deliveryOption, ShippingDetailsReadModel shippingDetails, String cartPrice, String totalPrice, String currency, int size, String submissionDate) {
        this.id = id;
        this.userId = userId;
        this.recipientName = recipientName;
        this.positions = positions;
        this.status = status;
        this.deliveryOption = deliveryOption;
        this.shippingDetails = shippingDetails;
        this.cartPrice = cartPrice;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.size = size;
        this.submissionDate = submissionDate;
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

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public List<OrderPositionReadModel> getPositions() {
        return positions;
    }

    public void setPositions(List<OrderPositionReadModel> positions) {
        this.positions = positions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryOptionReadModel getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOptionReadModel deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public ShippingDetailsReadModel getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetailsReadModel shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}