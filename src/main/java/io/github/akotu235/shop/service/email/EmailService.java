package io.github.akotu235.shop.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String recipient, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}