package io.github.akotu235.shop.service.shop.projection.read;

public class ShippingDetailsReadModel {
    private String country;
    private String street;
    private String city;
    private String postalCode;
    private String phone;

    public ShippingDetailsReadModel(String country, String street, String city, String postalCode, String phone) {
        this.country = country;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}