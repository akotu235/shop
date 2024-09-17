package io.github.akotu235.shop.service.shop.repository;

import io.github.akotu235.shop.service.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Page<Product> findByEnabledTrueAndAvailableQuantityGreaterThan(int availableQuantity, Pageable page);

    Page<Product> findByEnabledTrueAndAvailableQuantityGreaterThanAndCategoryId(int availableQuantity, Long categoryId, Pageable page);

    Page<Product> findByNameOrCategoryNameContainingIgnoreCase(Pageable page, String name);

    Optional<Product> findProductById(Long id);

    Product save(Product entity);
}