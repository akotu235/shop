package io.github.akotu235.shop.service.shop.repository;

import io.github.akotu235.shop.service.shop.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    Category save(Category entity);
}