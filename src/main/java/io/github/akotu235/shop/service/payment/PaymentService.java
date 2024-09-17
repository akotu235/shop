package io.github.akotu235.shop.service.payment;

import io.github.akotu235.shop.service.payment.model.Payment;
import io.github.akotu235.shop.service.payment.model.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    public Payment processPayment(double amount, String currency) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.PENDING);

        // Symulacja przetwarzania płatności
        boolean paymentSuccess = simulatePaymentProcessing();

        if (paymentSuccess) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return payment;
    }

    private boolean simulatePaymentProcessing() {
        // Symulacja, 70% szansy na sukces
        return Math.random() > 0.3;
    }
}