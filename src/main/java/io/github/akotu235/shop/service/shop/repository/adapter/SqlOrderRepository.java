package io.github.akotu235.shop.service.shop.repository.adapter;

import io.github.akotu235.shop.service.shop.entity.Order;
import io.github.akotu235.shop.service.shop.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlOrderRepository extends OrderRepository, JpaRepository<Order, Long> {
}