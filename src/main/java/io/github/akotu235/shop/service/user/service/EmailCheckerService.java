package io.github.akotu235.shop.service.user.service;

import io.github.akotu235.shop.properties.AppConfigurationProperties;
import io.github.akotu235.shop.exceptions.InvalidAddressException;
import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.email.EmailService;
import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.service.user.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailCheckerService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AppConfigurationProperties properties;
    private final Map<String, User> linkMap = new HashMap<>();
    private final MessageSource messageSource;

    public EmailCheckerService(EmailService emailService, UserRepository userRepository, AppConfigurationProperties properties, MessageSource messageSource) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.properties = properties;
        this.messageSource = messageSource;
    }

    public Result<User> confirm(String token) {
        User user = linkMap.get(token);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
            linkMap.remove(token);
            return new Result<>(true, user, "success.user-activation", user.getUsername());
        } else throw new InvalidAddressException("error.invalid-address");
    }

    public void sendVerificationEmail(User user) {
        String registrationLink = generateToken(user);
        emailService.sendEmail(user.getEmail(), messageSource.getMessage("user.activation.email.subject", null, user.getLocale()), getMessage(user, registrationLink));
    }

    private String getMessage(User user, String token) {
        Object[] args = new Object[]{user.getUsername(), properties.getTitle(), properties.getUrl(), token};
        return messageSource.getMessage("user.activation.email.text", args, user.getLocale());
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        linkMap.put(token, user);
        return token;
    }
}
