package io.github.akotu235.shop.service.shop.projection.read;

public class ProductReadModel {
    private Long id;
    private String name;
    private String description;
    private String price;
    private String currency;
    private int availableQuantity;
    private int photosQuantity;
    private boolean enabled;
    private CategoryReadModel category;

    public ProductReadModel(Long id, String name, String description, String price, String currency, int availableQuantity, int photosQuantity, boolean enabled, CategoryReadModel category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.availableQuantity = availableQuantity;
        this.photosQuantity = photosQuantity;
        this.enabled = enabled;
        this.category = category;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public CategoryReadModel getCategory() {
        return category;
    }

    public void setCategory(CategoryReadModel category) {
        this.category = category;
    }
}