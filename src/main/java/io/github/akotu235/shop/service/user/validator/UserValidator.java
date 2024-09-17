package io.github.akotu235.shop.service.user.validator;


import io.github.akotu235.shop.properties.PasswordConfigurationProperties;
import io.github.akotu235.shop.service.user.projection.UserWriteModel;
import io.github.akotu235.shop.service.user.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordConfigurationProperties passwordConfigurationProperties;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public UserValidator(UserRepository userRepository, MessageSource messageSource, PasswordConfigurationProperties passwordConfigurationProperties) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.passwordConfigurationProperties = passwordConfigurationProperties;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserWriteModel.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserWriteModel user = (UserWriteModel) target;

        if (userRepository.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "error.user", messageSource.getMessage("error.user.username-already-exist", new Object[]{user.getUsername()}, user.getLocale()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            errors.rejectValue("email", "error.user", messageSource.getMessage("error.user.email-already-exist", new Object[]{user.getEmail()}, user.getLocale()));
        }

        if (user.getUsername().length() > 50) {
            errors.rejectValue("username", "error.user", messageSource.getMessage("error.user.username-too-long", null, user.getLocale()));
        }

        if (user.getUsername().length() < 3) {
            errors.rejectValue("username", "error.user", messageSource.getMessage("error.user.username-too-short", null, user.getLocale()));
        }

        if (user.getUsername().matches(".*\\s+.*")) {
            errors.rejectValue("username", "error.user", messageSource.getMessage("error.user.username-contains-whitespaces", null, user.getLocale()));
        }

        if (user.getName().length() > 50) {
            errors.rejectValue("name", "error.user", messageSource.getMessage("error.user.name-too-long", null, user.getLocale()));
        }

        if (user.getName().length() < 3) {
            errors.rejectValue("name", "error.user", messageSource.getMessage("error.user.name-too-short", null, user.getLocale()));
        }

        if (user.getSurname().length() > 100) {
            errors.rejectValue("surname", "error.user", messageSource.getMessage("error.user.surname-too-long", null, user.getLocale()));
        }

        if (user.getSurname().length() < 3) {
            errors.rejectValue("surname", "error.user", messageSource.getMessage("error.user.surname-too-short", null, user.getLocale()));
        }

        if (user.getSurname().matches(".*\\s+.*")) {
            errors.rejectValue("surname", "error.user", messageSource.getMessage("error.user.surname-contains-whitespaces", null, user.getLocale()));
        }

        if (user.getEmail()==null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.rejectValue("email", "error.user", messageSource.getMessage("error.user.invalid-email", null, user.getLocale()));
        }

        if(user.getPassword().length() < passwordConfigurationProperties.getMinLength()){
            errors.rejectValue("password", "error.user", messageSource.getMessage("error.user.password-too-short", new Object[]{passwordConfigurationProperties.getMinLength()}, user.getLocale()));
        }

        if(passwordConfigurationProperties.isRequireNumeric() && !user.getPassword().matches(".*\\d.*")){
            errors.rejectValue("password", "error.user", messageSource.getMessage("error.user.password-no-digit", null, user.getLocale()));
        }

        if(passwordConfigurationProperties.isRequireUppercase() && !user.getPassword().matches(".*[A-Z].*")){
            errors.rejectValue("password", "error.user", messageSource.getMessage("error.user.password-no-capital-letters", null, user.getLocale()));
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.user", messageSource.getMessage("error.user.password-not-match", null, user.getLocale()));
        }
    }
}