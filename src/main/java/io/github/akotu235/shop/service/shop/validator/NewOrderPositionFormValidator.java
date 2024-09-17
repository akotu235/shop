package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.entity.Order;
import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.projection.write.OrderPositionWriteModel;
import io.github.akotu235.shop.service.shop.repository.OrderRepository;
import io.github.akotu235.shop.service.shop.repository.ProductRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;
import java.util.Optional;

@Component
public class NewOrderPositionFormValidator implements Validator {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MessageSource messageSource;

    public NewOrderPositionFormValidator(OrderRepository orderRepository, ProductRepository productRepository, MessageSource messageSource) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.messageSource = messageSource;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return OrderPositionWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderPositionWriteModel orderPositionWriteModel = (OrderPositionWriteModel) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        Optional<Order> orderOptional = orderRepository.findById(orderPositionWriteModel.getOrderId());
        if (orderOptional.isEmpty()) {
            errors.rejectValue("orderId", "error.order-position", messageSource.getMessage("error.order-position.order-not-exist", null, userLocale));
        } else {
            Optional<Product> productOptional = productRepository.findProductById(orderPositionWriteModel.getProductId());
            if (productOptional.isEmpty()) {
                errors.rejectValue("productId", "error.order-position", messageSource.getMessage("error.order-position.product-not-exist", null, userLocale));
            } else {
                if (productOptional.get().getAvailableQuantity() < orderPositionWriteModel.getQuantity() || orderPositionWriteModel.getQuantity() < 1) {
                    errors.rejectValue("quantity", "error.order-position", messageSource.getMessage("error.order-position.quantity", null, userLocale));
                }
            }
        }
    }
}