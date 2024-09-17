package io.github.akotu235.shop.service.shop.service;

import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;
import io.github.akotu235.shop.service.shop.projection.read.DeliveryOptionReadModel;
import io.github.akotu235.shop.service.shop.properties.DeliveryPricesConfigurationProperties;
import io.github.akotu235.shop.service.shop.properties.DeliveryWaitingDaysConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DeliveryService {
    private final DeliveryPricesConfigurationProperties deliveryPrices;
    private final DeliveryWaitingDaysConfigurationProperties deliveryWaitingDays;
    List<DeliveryOptionReadModel> deliveryOptions;

    public DeliveryService(DeliveryPricesConfigurationProperties deliveryPrices, DeliveryWaitingDaysConfigurationProperties deliveryWaitingDays) {
        this.deliveryPrices = deliveryPrices;
        this.deliveryWaitingDays = deliveryWaitingDays;
        setDeliveryOptions();
    }

    private void setDeliveryOptions() {
        deliveryOptions = Arrays.asList(
                new DeliveryOptionReadModel("delivery.standard", deliveryPrices.getStandard(), deliveryWaitingDays.getStandard(), DeliveryMethod.STANDARD),
                new DeliveryOptionReadModel("delivery.express", deliveryPrices.getExpress(), deliveryWaitingDays.getExpress(), DeliveryMethod.EXPRESS),
                new DeliveryOptionReadModel("delivery.pickup", deliveryPrices.getPickup(), deliveryWaitingDays.getPickup(), DeliveryMethod.PICKUP)
        );
    }

    public List<DeliveryOptionReadModel> getDeliveryOptions() {
        return deliveryOptions;
    }

    public DeliveryOptionReadModel getDeliveryOptionReadModel(DeliveryMethod deliveryMethod) {
        return deliveryOptions.stream().filter(option -> option.getDeliveryMethod() == deliveryMethod).findFirst().orElse(new DeliveryOptionReadModel());
    }
}