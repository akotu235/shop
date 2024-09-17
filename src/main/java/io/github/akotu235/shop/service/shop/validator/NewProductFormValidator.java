package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.projection.write.NewProductWriteModel;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class NewProductFormValidator implements Validator {

    private final MessageSource messageSource;

    public NewProductFormValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewProductWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewProductWriteModel product = (NewProductWriteModel) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        if (product.getPrice() <= 0) {
            errors.rejectValue("price", "error.product", messageSource.getMessage("error.product.incorrect-price", null, userLocale));
        }

        if (product.getAvailableQuantity() <= 0) {
            errors.rejectValue("availableQuantity", "error.product", messageSource.getMessage("error.product.incorrect-quantity", null, userLocale));
        }

        if (product.getName().isBlank()) {
            errors.rejectValue("name", "error.product", messageSource.getMessage("error.product.blank-name", null, userLocale));
        }

        if (product.getCategory().isBlank()) {

        }
    }
}