package io.github.akotu235.shop.service.shop.projection.read;

public class CartPositionReadModel {
    private ProductReadModel product;
    private int quantity;
    private String totalPrice;
    private Boolean hasError;
    private String errorMessage;

    public CartPositionReadModel() {}

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

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}