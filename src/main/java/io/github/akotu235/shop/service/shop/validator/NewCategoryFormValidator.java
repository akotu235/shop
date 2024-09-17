package io.github.akotu235.shop.service.shop.validator;


import io.github.akotu235.shop.service.shop.projection.write.NewCategoryWriteModel;
import io.github.akotu235.shop.service.shop.repository.CategoryRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class NewCategoryFormValidator implements Validator {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    public NewCategoryFormValidator(CategoryRepository categoryRepository, MessageSource messageSource) {
        this.categoryRepository = categoryRepository;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewCategoryWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewCategoryWriteModel category = (NewCategoryWriteModel) target;
        Locale userLocale = LocaleContextHolder.getLocale();

        if (categoryRepository.existsByName(category.getName())) {
            errors.rejectValue("name", "error.category", messageSource.getMessage("error.category.already-exist", new Object[]{category.getName()}, userLocale));
        }

        if (category.getName().isBlank()) {
            errors.rejectValue("name", "error.category", messageSource.getMessage("error.category.blank-name", new Object[]{category.getName()}, userLocale));
        }
    }
}