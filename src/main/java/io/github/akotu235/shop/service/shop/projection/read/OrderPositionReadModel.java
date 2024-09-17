package io.github.akotu235.shop.service.shop.projection.read;

public class OrderPositionReadModel {
    private Long orderId;
    private ProductReadModel product;
    private int quantity;
    private String totalPrice;

    public OrderPositionReadModel(Long order, ProductReadModel product, int quantity, String totalPrice) {
        this.orderId = order;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long order) {
        this.orderId = order;
    }

    public ProductReadModel getProduct() {
        return product;
    }

    public void setProduct(ProductReadModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}