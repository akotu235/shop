package io.github.akotu235.shop.service.shop.entity;

import io.github.akotu235.shop.service.shop.projection.write.NewProductWriteModel;
import jakarta.persistence.*;
import jakarta.validation.Valid;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String currency;

    private int availableQuantity;

    private int photosQuantity;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product(@Valid NewProductWriteModel newProduct, Category category) {
        this.name = newProduct.getName();
        this.description = newProduct.getDescription();
        this.price = newProduct.getPrice();
        this.currency = newProduct.getCurrency();
        this.availableQuantity = newProduct.getAvailableQuantity();
        this.photosQuantity = 1;
        this.enabled = true;
        this.category = category;
    }

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getPhotosQuantity() {
        return photosQuantity;
    }

    public void setPhotosQuantity(int photosQuantity) {
        this.photosQuantity = photosQuantity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}