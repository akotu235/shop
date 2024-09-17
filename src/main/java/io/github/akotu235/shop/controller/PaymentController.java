package io.github.akotu235.shop.controller;

import io.github.akotu235.shop.service.payment.model.PaymentRequest;
import io.github.akotu235.shop.service.shop.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final ShopService shopService;

    public PaymentController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/form")
    public ModelAndView showPaymentForm(Model model, Authentication authentication) {
        model.addAttribute("paymentRequest", shopService.getPaymentRequest(authentication));
        return new ModelAndView("payment-form");
    }

    @PostMapping("/process")
    public ModelAndView processPayment(@ModelAttribute PaymentRequest paymentRequest, Model model) {
        model.addAttribute("result", shopService.processPayment(paymentRequest));
        return new ModelAndView("result");
    }
}