package io.github.akotu235.shop.service.shop;

import io.github.akotu235.shop.exceptions.AccessDeniedException;
import io.github.akotu235.shop.exceptions.AppException;
import io.github.akotu235.shop.exceptions.ShopOperationException;
import io.github.akotu235.shop.properties.AppConfigurationProperties;
import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.email.EmailService;
import io.github.akotu235.shop.service.payment.PaymentService;
import io.github.akotu235.shop.service.payment.model.Payment;
import io.github.akotu235.shop.service.payment.model.PaymentRequest;
import io.github.akotu235.shop.service.payment.model.PaymentStatus;
import io.github.akotu235.shop.service.shop.entity.Category;
import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;
import io.github.akotu235.shop.service.shop.entity.Order;
import io.github.akotu235.shop.service.shop.entity.OrderStatus;
import io.github.akotu235.shop.service.shop.projection.read.*;
import io.github.akotu235.shop.service.shop.projection.write.*;
import io.github.akotu235.shop.service.shop.properties.ShopConfigurationProperties;
import io.github.akotu235.shop.service.shop.service.CategoryService;
import io.github.akotu235.shop.service.shop.service.DeliveryService;
import io.github.akotu235.shop.service.shop.service.OrderService;
import io.github.akotu235.shop.service.shop.service.ProductService;
import io.github.akotu235.shop.service.shop.util.ProjectionUtil;
import io.github.akotu235.shop.service.shop.validator.OrderValidator;
import io.github.akotu235.shop.service.user.UserService;
import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.util.FormatUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ShopService {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final EmailService emailService;
    private final MessageSource messageSource;
    private final ShopConfigurationProperties config;
    private final AppConfigurationProperties appConfig;
    private final OrderValidator orderValidator;


    public ShopService(ProductService productService, CategoryService categoryService, OrderService orderService, DeliveryService deliveryService, PaymentService paymentService, UserService userService, EmailService emailService, MessageSource messageSource, ShopConfigurationProperties config, AppConfigurationProperties appConfig, OrderValidator orderValidator) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.emailService = emailService;
        this.messageSource = messageSource;
        this.config = config;
        this.appConfig = appConfig;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Result<ProductReadModel> createProduct(@Valid NewProductWriteModel newProduct) {
        ProductReadModel product = ProjectionUtil.getProductReadModel(productService.createProduct(newProduct));
        return new Result<>(true, product, "success.product-creation", product.getName());
    }

    public Page<ProductReadModel> getAllProducts(Pageable page) {
        return ProjectionUtil.getProductsReadModel(productService.getAllProducts(page));
    }

    public Page<ProductReadModel> getProductsByCategoryName(Pageable page, String categoryName) {
        return ProjectionUtil.getProductsReadModel(productService.getProductsByCategoryName(page, categoryName));
    }

    public ProductReadModel getProduct(String productId) {
        return ProjectionUtil.getProductReadModel(productService.getProduct(productId));
    }


    public ResponseEntity<InputStreamResource> getProductPhoto(Long productId, Long photoId) {
        return productService.getProductPhoto(productId, photoId);
    }

    public Result<CategoryReadModel> createCategory(@Valid NewCategoryWriteModel newCategory) {
        Category category = categoryService.createCategory(newCategory);
        return new Result<>(true, ProjectionUtil.getCategoryReadModel(category), "success.category-creation", category.getName());
    }

    public List<CategoryReadModel> getCategories() {
        return ProjectionUtil.getCategoriesReadModel(categoryService.getCategories());
    }

    public Category getCategory(String name) {
        return categoryService.getCategory(name);
    }

    public void addOrderPosition(@Valid OrderPositionWriteModel newOrderPosition, Authentication authentication) {
        OrderReadModel cart = getCart(authentication);
        if (Objects.equals(cart.getId(), newOrderPosition.getOrderId())) {
            orderService.addOrderPosition(newOrderPosition);
        } else throw new AccessDeniedException("error.access-denied");
    }

    public List<OrderPositionReadModel> getOrderPositionsReadModel(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order.getPositions().stream().map(orderPosition -> ProjectionUtil.getOrderPositionReadModel(orderPosition, orderPosition.getQuantity() * orderPosition.getProduct().getPrice())).toList();
    }

    public OrderReadModel getCart(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Order order = orderService.getCart(userService.getUser(authentication));
            return getOrderReadModel(order);
        } else throw new AccessDeniedException("error.access-denied");
    }

    public OrderPositionWriteModel getNewOrderPositionWriteModel(Authentication authentication, String productId) {
        if (authentication != null) {
            return new OrderPositionWriteModel(getCartId(authentication), Long.parseLong(productId), 1);
        } else {
            return new OrderPositionWriteModel();
        }
    }

    public Double calculateOrderTotalPrice(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        double deliveryCost = getDeliveryOptionReadModel(order.getDeliveryMethod()).getPrice();
        return FormatUtils.roundPriceToTwoDecimalPlaces(calculateCartTotalPrice(orderId) + deliveryCost);

    }

    public Double calculateCartTotalPrice(Long orderId) {
        return FormatUtils.roundPriceToTwoDecimalPlaces(orderService.calculateOrderPositionsTotalPrice(orderId));
    }

    public void removeFromCart(String productId, Authentication authentication) {
        if (authentication != null) {
            orderService.removePosition(getCartId(authentication), Long.parseLong(productId));
        } else throw new AccessDeniedException("error.access-denied");
    }

    private Long getCartId(Authentication authentication) {
        return orderService.getCart(userService.getUser(authentication)).getId();
    }

    public Result<ProductReadModel> disableProduct(String productId) {
        return new Result<>(true, ProjectionUtil.getProductReadModel(productService.setEnableProduct(Long.parseLong(productId), false)), "success.product-disable");
    }

    public Result<ProductReadModel> enableProduct(String productId) {
        return new Result<>(true, ProjectionUtil.getProductReadModel(productService.setEnableProduct(Long.parseLong(productId), true)), "success.product-enable");
    }

    public void setPositionQuantity(@Valid OrderPositionWriteModel newOrderPosition, Authentication authentication) {
        if (getCart(authentication) != null) {
            orderService.setPositionQuantity(newOrderPosition);
        } else throw new AccessDeniedException("error.access-denied");
    }

    public void setDeliveryMethod(DeliveryOptionWriteModel deliveryOptionWriteModel, Authentication authentication) {
        if (authentication != null) {
            orderService.setDeliveryMethod(getCartId(authentication), deliveryOptionWriteModel);
        } else throw new AccessDeniedException("error.access-denied");
    }

    private OrderReadModel getOrderReadModel(Order order) {
        return ProjectionUtil.getOrderReadModel(order, getOrderPositionsReadModel(order.getId()), calculateCartTotalPrice(order.getId()), calculateOrderTotalPrice(order.getId()), config.getCurrency(), getDeliveryOptionReadModel(order.getDeliveryMethod()), ProjectionUtil.getShippingDetailsReadModel(order.getShippingDetails()));
    }

    private DeliveryOptionReadModel getDeliveryOptionReadModel(DeliveryMethod deliveryMethod) {
        return deliveryService.getDeliveryOptionReadModel(deliveryMethod);
    }

    public List<DeliveryOptionReadModel> getDeliveryOptions() {
        return deliveryService.getDeliveryOptions();
    }

    public Page<ProductReadModel> searchProducts(Pageable page, String name) {
        return ProjectionUtil.getProductsReadModel(productService.searchProductsByName(page, name));
    }

    public ShippingDetailsWriteModel getShippingDetailsWriteModel(Authentication authentication) {
        OrderReadModel order = getCart(authentication);
        return ProjectionUtil.getShippingDetailsWriteModel(order.getShippingDetails());
    }

    public void setShippingDetails(@Valid ShippingDetailsWriteModel shippingDetails, Authentication authentication) {
        orderService.setShippingDetails(getCartId(authentication), shippingDetails);
    }

    @Transactional(rollbackOn = AppException.class)
    public PaymentRequest getPaymentRequest(Authentication authentication) {
        OrderReadModel order = getCart(authentication);
        if (order.getStatus().equals(OrderStatus.PENDING)) {
            Errors errors = new BeanPropertyBindingResult(order, "order");
            orderValidator.validate(order, errors);
            if (errors.hasErrors()) {
                throw new ShopOperationException("error.invalid-order");
            }
        }
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(calculateOrderTotalPrice(order.getId()));
        paymentRequest.setCurrency(order.getCurrency());
        paymentRequest.setOrderId(order.getId());
        if (order.getStatus().equals(OrderStatus.PENDING)) {
            productService.reserveProducts(order.getPositions());
            orderService.setOrderProcessing(order.getId());
        }
        return paymentRequest;
    }

    public Result<Payment> processPayment(PaymentRequest paymentRequest) {
        Order order = orderService.getOrderById(paymentRequest.getOrderId());
        if (order.getStatus().equals(OrderStatus.PROCESSING)) {
            Payment payment = paymentService.processPayment(paymentRequest.getAmount(), paymentRequest.getCurrency());
            if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
                orderService.setOrderConfirmed(paymentRequest.getOrderId());
                sendEmailConfirmingPurchase(order.getUser(), String.valueOf(order.getId()));
                return new Result<>(true, payment, "payment.success");
            } else {
                return new Result<>(false, payment, "payment.failure");
            }
        }
        return new Result<>(false, null, "payment.failure.time-exceeded");
    }

    private void sendEmailConfirmingPurchase(User user, String orderId) {
        emailService.sendEmail(user.getEmail(), messageSource.getMessage("user.confirming-purchase.email.subject", null, user.getLocale()), getThankYouMessage(user, orderId));
    }

    private String getThankYouMessage(User user, String orderId) {
        String link = appConfig.getUrl() + "/user/" + user.getUsername() + "/orders/" + orderId;
        Object[] args = new Object[]{user.getName(), link, appConfig.getTitle()};
        return messageSource.getMessage("user.confirming-purchase.email.text", args, user.getLocale());
    }

    public List<OrderReadModel> getUserOrders(Authentication authentication) {
        var user = userService.getUser(authentication);
        return orderService.getOrders(user).stream()
                .filter(order -> order.getStatus() != OrderStatus.PENDING)
                .sorted(Comparator.comparing(Order::getModificationDate).reversed())
                .map(this::getOrderReadModel)
                .toList();
    }

    public OrderReadModel getUserOrder(String orderId, Authentication authentication) {
        User user = userService.getUser(authentication);
        Order order = orderService.getOrderById(Long.valueOf(orderId));
        if (order.getUser().equals(user)) {
            return getOrderReadModel(order);
        } else throw new AccessDeniedException("error.access-denied");
    }

    public List<OrderReadModel> getOrders(OrderStatus status) {
        return orderService.findOrdersByStatus(status).stream().map(this::getOrderReadModel).toList();
    }

    public List<OrderReadModel> getAllOrders() {
        return orderService.getAllOrders().stream().map(this::getOrderReadModel).toList();
    }

    public OrderReadModel getOrderById(String orderId) {
        return getOrderReadModel(orderService.getOrderById(Long.valueOf(orderId)));
    }

    public void updateStatus(Long orderId, OrderStatus newStatus) {
        orderService.updateStatus(orderId, newStatus);
    }
}