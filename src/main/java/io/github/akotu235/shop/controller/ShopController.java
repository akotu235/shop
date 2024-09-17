package io.github.akotu235.shop.controller;

import io.github.akotu235.shop.service.shop.ShopService;
import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;
import io.github.akotu235.shop.service.shop.projection.read.RequestParamsReadModel;
import io.github.akotu235.shop.service.shop.projection.write.DeliveryOptionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.OrderPositionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.ShippingDetailsWriteModel;
import io.github.akotu235.shop.service.shop.validator.AddressValidator;
import io.github.akotu235.shop.service.shop.validator.NewOrderPositionFormValidator;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShopController {
    private final ShopService shopService;
    private final NewOrderPositionFormValidator newOrderPositionFormValidator;
    private final AddressValidator addressValidator;

    public ShopController(ShopService shopService, NewOrderPositionFormValidator newOrderPositionFormValidator, AddressValidator addressValidator) {
        this.shopService = shopService;
        this.newOrderPositionFormValidator = newOrderPositionFormValidator;
        this.addressValidator = addressValidator;
    }

    @GetMapping("/")
    public String getAllProducts(@RequestParam(defaultValue = "") String name,
                                 @RequestParam(defaultValue = "all") String category,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "4") int size,
                                 @RequestParam(defaultValue = "name") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortDirection,
                                 Model model) {
        if (size > 20) {
            size = 20;
        }
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        if (!name.isBlank()) {
            category = "all";
            model.addAttribute("page", shopService.searchProducts(pageable, name));
        } else if (!category.equals("all")) {
            model.addAttribute("page", shopService.getProductsByCategoryName(pageable, category));
        } else {
            model.addAttribute("page", shopService.getAllProducts(pageable));
        }
        model.addAttribute("params", new RequestParamsReadModel(name, category, page, size, sortBy, sortDirection));
        return "shop";
    }

    @GetMapping("/products/{productId}")
    public String getProduct(@PathVariable String productId,
                             Authentication authentication,
                             Model model) {
        model.addAttribute("product", shopService.getProduct(productId));
        if (!model.containsAttribute("position")) {
            model.addAttribute("position", shopService.getNewOrderPositionWriteModel(authentication, productId));
        }
        return "product";
    }

    @GetMapping("/products/{productId}/photo/{photoId}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long productId,
                                                        @PathVariable Long photoId) {
        return shopService.getProductPhoto(productId, photoId);
    }

    @GetMapping("/cart")
    public String getCart() {
        return "cart";
    }

    @GetMapping("/categories")
    public String getCategories() {
        return "categories";
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("position") @Valid OrderPositionWriteModel position,
                            BindingResult bindingResult,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        newOrderPositionFormValidator.validate(position, bindingResult);
        if (!bindingResult.hasErrors()) {
            shopService.addOrderPosition(position, authentication);
        }
        position.setQuantity(1);
        redirectAttributes.addFlashAttribute("position", position);
        return "redirect:/products/" + position.getProductId();
    }

    @PostMapping("/cart/{productId}/set-quantity")
    public void setQuantity(@PathVariable String productId,
                            @RequestParam String quantity,
                            Authentication authentication) {
        OrderPositionWriteModel position = shopService.getNewOrderPositionWriteModel(authentication, productId);
        position.setQuantity(Integer.parseInt(quantity));
        Errors errors = new BeanPropertyBindingResult(position, "position");
        newOrderPositionFormValidator.validate(position, errors);
        if (!errors.hasErrors()) {
            shopService.setPositionQuantity(position, authentication);
        }
    }

    @PostMapping("/cart/delete/{productId}")
    public String removeFromCart(@PathVariable String productId, Authentication authentication) {
        shopService.removeFromCart(productId, authentication);
        return "redirect:/cart";
    }

    @GetMapping("/order/summary")
    public String getOrderSummary(Model model, Authentication authentication) {
        model.addAttribute("order", shopService.getCart(authentication));
        return "order-summary";
    }

    @GetMapping("/order/set-delivery-method")
    public String getDeliveryMethodForm(Model model) {
        model.addAttribute("delivery", new DeliveryOptionWriteModel());
        model.addAttribute("deliveryOptions", shopService.getDeliveryOptions());
        return "select-delivery-method";
    }

    @PostMapping("/order/set-delivery-method")
    public String setDeliveryMethod(@ModelAttribute("delivery") DeliveryOptionWriteModel delivery,
                                    Authentication authentication) {
        shopService.setDeliveryMethod(delivery, authentication);
        if (delivery.getDeliveryMethod().equals(DeliveryMethod.PICKUP)) {
            return "redirect:/order/summary";
        } else return "redirect:/order/set-shipping-address";
    }

    @GetMapping("/order/set-shipping-address")
    public String getShippingAddressForm(Model model, Authentication authentication) {
        model.addAttribute("shippingDetails", shopService.getShippingDetailsWriteModel(authentication));
        return "set-shipping-address-form";
    }

    @PostMapping("/order/set-shipping-address")
    public String setShippingAddress(@ModelAttribute("shippingDetails") @Valid ShippingDetailsWriteModel shippingDetails,
                                     Authentication authentication,
                                     BindingResult bindingResult) {
        addressValidator.validate(shippingDetails, bindingResult);
        if (bindingResult.hasErrors()) {
            return "set-shipping-address-form";
        }
        shopService.setShippingDetails(shippingDetails, authentication);
        return "redirect:/order/summary";
    }

    @GetMapping("/user/{username}/orders")
    @PreAuthorize("#username == authentication.name")
    public String getUserPanel(
            Model model,
            Authentication authentication,
            @PathVariable String username) {
        model.addAttribute("orders", shopService.getUserOrders(authentication));
        return "user-orders";
    }

    @GetMapping("/user/{username}/orders/{orderId}")
    @PreAuthorize("#username == authentication.name")
    public String getOrderDetails(@PathVariable String orderId,
                                  @PathVariable String username,
                                  Authentication authentication,
                                  Model model) {
        model.addAttribute("order", shopService.getUserOrder(orderId, authentication));
        return "order-details";
    }
}