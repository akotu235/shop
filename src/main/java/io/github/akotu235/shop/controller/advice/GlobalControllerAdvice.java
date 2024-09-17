package io.github.akotu235.shop.controller.advice;

import io.github.akotu235.shop.service.shop.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final ShopService shopService;

    public GlobalControllerAdvice(ShopService shopService) {
        this.shopService = shopService;
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("categories", shopService.getCategories());
        if (authentication != null) {
            model.addAttribute("cart", shopService.getCart(authentication));
        }
    }
}