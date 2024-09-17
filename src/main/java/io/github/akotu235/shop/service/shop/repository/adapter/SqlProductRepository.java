package io.github.akotu235.shop.service.shop.repository.adapter;

import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlProductRepository extends ProductRepository, JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByNameOrCategoryNameContainingIgnoreCase(Pageable page, @Param("name") String name);
}