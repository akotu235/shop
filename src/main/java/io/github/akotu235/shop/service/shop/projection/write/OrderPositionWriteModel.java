package io.github.akotu235.shop.service.shop.projection.write;

public class OrderPositionWriteModel {
    private Long orderId;
    private Long productId;
    private int quantity;

    public OrderPositionWriteModel() {
        this.orderId = -1L;
        this.productId = -1L;
        this.quantity = 0;
    }

    public OrderPositionWriteModel(Long orderId, Long productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}