package io.github.akotu235.shop.service.shop.projection.write;

import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;

public class DeliveryOptionWriteModel {
    DeliveryMethod deliveryMethod;

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}