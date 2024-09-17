package io.github.akotu235.shop.service.shop.repository;

import io.github.akotu235.shop.service.shop.entity.Order;
import io.github.akotu235.shop.service.shop.entity.OrderStatus;
import io.github.akotu235.shop.service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();

    Optional<Order> findById(Long id);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    List<Order> findByUser(User user);

    Order save(Order entity);
}