package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.projection.read.OrderPositionReadModel;
import io.github.akotu235.shop.service.shop.projection.read.OrderReadModel;
import io.github.akotu235.shop.service.shop.repository.ProductRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;
import java.util.Optional;

@Component
public class OrderValidator implements Validator {

    private final ProductRepository productRepository;
    private final MessageSource messageSource;

    public OrderValidator(ProductRepository productRepository, MessageSource messageSource) {
        this.productRepository = productRepository;
        this.messageSource = messageSource;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return OrderReadModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderReadModel order = (OrderReadModel) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        for (OrderPositionReadModel position : order.getPositions()) {
            Optional<Product> productOptional = productRepository.findProductById(position.getProduct().getId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (position.getQuantity() > product.getAvailableQuantity()) {
                    errors.rejectValue("positions", "error.order-position", messageSource.getMessage("error.order-position.quantity", null, userLocale));
                }

                if(!position.getProduct().isEnabled()){
                    errors.rejectValue("positions", "error.order-position", messageSource.getMessage("error.order-position.product-not-available", null, userLocale));
                }
            } else {
                errors.rejectValue("positions", "error.order-position", messageSource.getMessage("error.order-position.product-not-exist", null, userLocale));
            }
        }
    }
}