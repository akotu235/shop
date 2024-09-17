package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.projection.write.ShippingDetailsWriteModel;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class AddressValidator implements Validator {

    private final MessageSource messageSource;

    public AddressValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ShippingDetailsWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ShippingDetailsWriteModel shippingDetails = (ShippingDetailsWriteModel) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        if (shippingDetails.getCountry() == null || !shippingDetails.getCountry().matches("[\\p{L} ]{2,}")) {
            String errorMessage = messageSource.getMessage("error.shipping-details.country.invalid", null, userLocale);
            errors.rejectValue("country", "error.shipping-details.country.invalid", errorMessage);
        }

        if (shippingDetails.getStreet() == null || !shippingDetails.getStreet().matches(".*[\\p{L}]{3,}.*")) {
            String errorMessage = messageSource.getMessage("error.shipping-details.address.invalid", null, userLocale);
            errors.rejectValue("address", "error.shipping-details.address.invalid", errorMessage);
        }

        if (shippingDetails.getCity() == null || !shippingDetails.getCity().matches("[\\p{L} ]{2,}")) {
            String errorMessage = messageSource.getMessage("error.shipping-details.city.invalid", null, userLocale);
            errors.rejectValue("city", "error.shipping-details.city.invalid", errorMessage);
        }

        if (shippingDetails.getPostalCode() == null || !shippingDetails.getPostalCode().matches("\\d{2}-\\d{3}")) {
            String errorMessage = messageSource.getMessage("error.shipping-details.postal-code.invalid", null, userLocale);
            errors.rejectValue("postalCode", "error.shipping-details.postal-code.invalid", errorMessage);
        }

        if (shippingDetails.getPhone() == null || !shippingDetails.getPhone().matches("\\+?\\d{9,15}")) {
            String errorMessage = messageSource.getMessage("error.shipping-details.phone.invalid", null, userLocale);
            errors.rejectValue("phone", "error.shipping-details.phone.invalid", errorMessage);
        }
    }
}