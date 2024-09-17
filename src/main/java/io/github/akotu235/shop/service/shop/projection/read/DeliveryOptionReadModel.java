package io.github.akotu235.shop.service.shop.projection.read;

import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;
import io.github.akotu235.shop.util.FormatUtils;

public class DeliveryOptionReadModel {
    private String translationCode;
    private String formattedPrice;
    private double price;
    private int waitingDays;
    private DeliveryMethod deliveryMethod;
    private boolean isPickup;

    public DeliveryOptionReadModel() {
    }

    public DeliveryOptionReadModel(String translationCode, double price, int waitingDays, DeliveryMethod deliveryMethod) {
        this.translationCode = translationCode;
        this.formattedPrice = FormatUtils.formatPrice(price);
        this.price = price;
        this.waitingDays = waitingDays;
        this.deliveryMethod = deliveryMethod;
        this.isPickup = deliveryMethod == DeliveryMethod.PICKUP;
    }

    public String getTranslationCode() {
        return translationCode;
    }

    public void setTranslationCode(String translationCode) {
        this.translationCode = translationCode;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWaitingDays() {
        return waitingDays;
    }

    public void setWaitingDays(int waitingDays) {
        this.waitingDays = waitingDays;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }
}