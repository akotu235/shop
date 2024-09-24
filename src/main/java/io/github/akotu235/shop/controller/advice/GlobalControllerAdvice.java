package io.github.akotu235.shop.controller.advice;

import io.github.akotu235.shop.service.shop.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final ShopService shopService;
    private final List<Locale> supportedLocales;

    public GlobalControllerAdvice(ShopService shopService, List<Locale> supportedLocales) {
        this.shopService = shopService;
        this.supportedLocales = supportedLocales;
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("categories", shopService.getCategories());
        model.addAttribute("supportedLanguages", supportedLocales);
        if (authentication != null) {
            model.addAttribute("cart", shopService.getCart(authentication));
        }
    }
}