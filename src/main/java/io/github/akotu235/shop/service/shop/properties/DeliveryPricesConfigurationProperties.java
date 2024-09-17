package io.github.akotu235.shop.service.shop.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("shop.delivery.price")
public class DeliveryPricesConfigurationProperties {
    private double standard;
    private double express;
    private double pickup;

    public double getStandard() {
        return standard;
    }

    public void setStandard(double standard) {
        this.standard = standard;
    }

    public double getExpress() {
        return express;
    }

    public void setExpress(double express) {
        this.express = express;
    }

    public double getPickup() {
        return pickup;
    }

    public void setPickup(double pickup) {
        this.pickup = pickup;
    }
}