package io.github.akotu235.shop.service.shop.repository.adapter;

import io.github.akotu235.shop.service.shop.entity.Category;
import io.github.akotu235.shop.service.shop.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlCategoryRepository extends CategoryRepository, JpaRepository<Category, Long> {
}