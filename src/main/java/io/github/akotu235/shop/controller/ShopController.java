package io.github.akotu235.shop.controller;

import io.github.akotu235.shop.service.shop.ShopService;
import io.github.akotu235.shop.service.shop.entity.DeliveryMethod;
import io.github.akotu235.shop.service.shop.projection.read.RequestParamsReadModel;
import io.github.akotu235.shop.service.shop.projection.write.DeliveryOptionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.OrderPositionWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.ShippingDetailsWriteModel;
import io.github.akotu235.shop.service.shop.validator.AddressValidator;
import io.github.akotu235.shop.service.shop.validator.NewOrderPositionFormValidator;
import jakarta.servlet.http.HttpSession;
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
    public String getShopPage(HttpSession session, Model model) {
        RequestParamsReadModel requestParams = (RequestParamsReadModel) session.getAttribute("params");
        if (requestParams == null) {
            requestParams = getDefaultParams();
        }
        Sort.Direction direction = Sort.Direction.fromString(requestParams.getSortDirection());
        Pageable pageable = PageRequest.of(requestParams.getPage(), requestParams.getSize(), Sort.by(direction, requestParams.getSortBy()));
        String name = requestParams.getName();
        String category = requestParams.getCategory();
        if (!name.isBlank()) {
            model.addAttribute("page", shopService.searchProducts(pageable, name));
        } else if (!category.equals("all")) {
            model.addAttribute("page", shopService.getProductsByCategoryName(pageable, category));
        } else {
            model.addAttribute("page", shopService.getAllProducts(pageable));
        }
        model.addAttribute("params", requestParams);
        return "shop";
    }

    @GetMapping("/set-params")
    public String setParams(@RequestParam(defaultValue = "") String name,
                            @RequestParam(defaultValue = "all") String category,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "4") int size,
                            @RequestParam(defaultValue = "name") String sortBy,
                            @RequestParam(defaultValue = "asc") String sortDirection,
                            HttpSession session) {
        size = Math.min(size, 20);
        RequestParamsReadModel requestParams = new RequestParamsReadModel(name, category, page, size, sortBy, sortDirection);
        session.setAttribute("params", requestParams);
        return "redirect:/";
    }

    @GetMapping("/all-products")
    public String getAllProductsRedirect(HttpSession session) {
        setCategoryParam(session, "all");
        return "redirect:/";
    }

    @GetMapping("/category/{category}")
    public String getCategoryRedirect(HttpSession session, @PathVariable String category) {
        setCategoryParam(session, category);
        return "redirect:/";
    }

    @GetMapping("/categories")
    public String getCategories() {
        return "categories";
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

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute("position") @Valid OrderPositionWriteModel position,
                            BindingResult bindingResult,
                            Authentication authentication) {
        newOrderPositionFormValidator.validate(position, bindingResult);
        if (!bindingResult.hasErrors()) {
            shopService.addOrderPosition(position, authentication);
        }
        position.setQuantity(1);
        return "redirect:/";
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
        model.addAttribute("order", shopService.getOrderSummary(authentication));
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

    private void setCategoryParam(HttpSession session, String category) {
        RequestParamsReadModel requestParams = (RequestParamsReadModel) session.getAttribute("params");
        if(requestParams == null){
            requestParams = getDefaultParams();
        }
        requestParams.setName("");
        requestParams.setCategory(category);
        requestParams.setPage(0);
        session.setAttribute("params", requestParams);
    }

    private RequestParamsReadModel getDefaultParams(){
        return new RequestParamsReadModel("", "all", 0, 4,"name", "asc");
    }
}