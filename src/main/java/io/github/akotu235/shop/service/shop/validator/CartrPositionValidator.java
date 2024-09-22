package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.entity.OrderPosition;
import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.projection.write.OrderPositionWriteModel;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class CartrPositionValidator implements Validator {

    private final MessageSource messageSource;

    public CartrPositionValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return OrderPositionWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderPosition orderPosition = (OrderPosition) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        validateProduct(orderPosition.getProduct(), errors, userLocale);
        validateOrderPositionQuantity(orderPosition, errors, userLocale);
    }

    private void validateProduct(Product product, Errors errors, Locale userLocale) {
        if (product == null) {
            String errorMessage = messageSource.getMessage("error.order-position.product-not-exist", null, userLocale);
            errors.reject("error.order-position", errorMessage);
        } else if (!product.isEnabled()) {
            String errorMessage = messageSource.getMessage("error.order-position.product-not-available", null, userLocale);
            errors.reject("error.order-position", errorMessage);
        }
    }

    private void validateOrderPositionQuantity(OrderPosition orderPosition, Errors errors, Locale userLocale) {
        Product product = orderPosition.getProduct();
        int orderedQuantity = orderPosition.getQuantity();
        int availableQuantity = product.getAvailableQuantity();

        if (isInvalidQuantity(orderedQuantity, availableQuantity)) {
            String errorMessage = messageSource.getMessage("error.order-position.quantity", new Object[]{availableQuantity}, userLocale);
            errors.reject("error.order-position", errorMessage);
        }
    }

    private boolean isInvalidQuantity(int orderedQuantity, int availableQuantity) {
        return orderedQuantity < 1 || orderedQuantity > availableQuantity;
    }
}