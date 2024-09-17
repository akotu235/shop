package io.github.akotu235.shop.service.shop.service;

import io.github.akotu235.shop.exceptions.AppException;
import io.github.akotu235.shop.exceptions.ShopOperationException;
import io.github.akotu235.shop.service.shop.entity.*;
import io.github.akotu235.shop.service.shop.projection.write.DeliveryOptionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.OrderPositionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.ShippingDetailsWriteModel;
import io.github.akotu235.shop.service.shop.repository.OrderRepository;
import io.github.akotu235.shop.service.user.model.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final TaskScheduler taskScheduler;

    public OrderService(OrderRepository orderRepository, ProductService productService, TaskScheduler taskScheduler) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.taskScheduler = taskScheduler;
    }

    public Order createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setPositions(new ArrayList<>());
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        Optional<Order> lastOrderOptional = getOrders(user).stream()
                .max(Comparator.comparing(Order::getModificationDate));
        ShippingDetails shippingDetails = new ShippingDetails();
        if (lastOrderOptional.isPresent()) {
            ShippingDetails lastShippingDetails = lastOrderOptional.get().getShippingDetails();
            shippingDetails.setCountry(lastShippingDetails.getCountry());
            shippingDetails.setStreet(lastShippingDetails.getStreet());
            shippingDetails.setCity(lastShippingDetails.getCity());
            shippingDetails.setPostalCode(lastShippingDetails.getPostalCode());
            shippingDetails.setPostalCode(lastShippingDetails.getPostalCode());
            shippingDetails.setPhone(lastShippingDetails.getPhone());
        } else {
            shippingDetails.setCountry("PL");
        }
        shippingDetails.setOrder(order);
        order.setShippingDetails(shippingDetails);
        return orderRepository.save(order);
    }

    public void addOrderPosition(@Valid OrderPositionWriteModel newOrderPosition) {
        Order order = getOrderById(newOrderPosition.getOrderId());
        Optional<OrderPosition> orderPositionOptional = getOrderPosition(newOrderPosition.getProductId(), order);
        OrderPosition orderPosition;
        if (orderPositionOptional.isPresent()) {
            orderPosition = orderPositionOptional.get();
            orderPosition.setQuantity(Math.min(orderPosition.getQuantity() + newOrderPosition.getQuantity(), orderPosition.getProduct().getAvailableQuantity()));
        } else {
            orderPosition = new OrderPosition();
            orderPosition.setOrder(order);
            orderPosition.setProduct(productService.getProduct(newOrderPosition.getProductId()));
            orderPosition.setQuantity(newOrderPosition.getQuantity());
            order.getPositions().add(orderPosition);
        }
        orderRepository.save(order);
    }

    private Optional<OrderPosition> getOrderPosition(Long productId, Order order) {
        return order.getPositions().stream()
                .filter(position -> position.getProduct().getId().equals(productId))
                .findFirst();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ShopOperationException("error.shop.order.not-found"));
    }

    @Transactional(rollbackOn = AppException.class)
    public Order getCart(User user) {
        List<Order> orders = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING);
        if (!orders.isEmpty()) {
            return orders.getFirst();
        }
        orders = orderRepository.findByUserAndStatus(user, OrderStatus.PROCESSING);
        if (!orders.isEmpty()) {
            return orders.getFirst();
        }
        return createOrder(user);
    }

    public List<Order> getOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public Double calculateOrderPositionsTotalPrice(Long orderId) {
        return getOrderById(orderId).getPositions().stream().mapToDouble(orderPosition -> orderPosition.getQuantity() * orderPosition.getProduct().getPrice()).sum();
    }

    public void removePosition(Long cartId, Long productId) {
        Order order = getOrderById(cartId);
        getOrderPosition(productId, order).ifPresent(orderPosition -> order.getPositions().remove(orderPosition));
        orderRepository.save(order);
    }

    public void setPositionQuantity(@Valid OrderPositionWriteModel newOrderPosition) {
        Order order = getOrderById(newOrderPosition.getOrderId());
        getOrderPosition(newOrderPosition.getProductId(), order).ifPresent(orderPosition -> {
            orderPosition.setQuantity(newOrderPosition.getQuantity());
        });
        orderRepository.save(order);
    }

    public void setDeliveryMethod(Long orderId, DeliveryOptionWriteModel deliveryOptionWriteModel) {
        Order order = getOrderById(orderId);
        order.setDeliveryMethod(deliveryOptionWriteModel.getDeliveryMethod());
        orderRepository.save(order);
    }

    public void setShippingDetails(Long cartId, @Valid ShippingDetailsWriteModel shippingDetailsWriteModel) {
        Order order = getOrderById(cartId);
        ShippingDetails shippingDetails = order.getShippingDetails();
        if (shippingDetails == null) {
            shippingDetails = new ShippingDetails();
            shippingDetails.setOrder(order);
            order.setShippingDetails(shippingDetails);
        }
        shippingDetails.setStreet(shippingDetailsWriteModel.getStreet());
        shippingDetails.setCity(shippingDetailsWriteModel.getCity());
        shippingDetails.setCountry(shippingDetailsWriteModel.getCountry());
        shippingDetails.setPostalCode(shippingDetailsWriteModel.getPostalCode());
        shippingDetails.setPhone(shippingDetailsWriteModel.getPhone());
        orderRepository.save(order);
    }

    @Transactional(rollbackOn = AppException.class)
    public void setOrderConfirmed(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PAID);
        order.setSubmissionDate(LocalDateTime.now());
        orderRepository.save(order);
    }


    @Transactional(rollbackOn = AppException.class)
    public void setOrderProcessing(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        taskScheduler.schedule(() -> processOrderAfterDelay(orderId),
                new java.util.Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2)));
    }

    @Transactional(rollbackOn = AppException.class)
    public void processOrderAfterDelay(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            List<OrderPosition> positions = order.getPositions();
            productService.cancelProductReservations(positions);
        }
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateStatus(Long orderId, OrderStatus newStatus) {
        getOrderById(orderId).setStatus(newStatus);
        orderRepository.save(getOrderById(orderId));
    }
}