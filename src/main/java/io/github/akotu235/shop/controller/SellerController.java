package io.github.akotu235.shop.controller;

import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.shop.ShopService;
import io.github.akotu235.shop.service.shop.entity.OrderStatus;
import io.github.akotu235.shop.service.shop.projection.read.CategoryReadModel;
import io.github.akotu235.shop.service.shop.projection.read.ProductReadModel;
import io.github.akotu235.shop.service.shop.projection.write.NewCategoryWriteModel;
import io.github.akotu235.shop.service.shop.projection.write.NewProductWriteModel;
import io.github.akotu235.shop.service.shop.validator.NewCategoryFormValidator;
import io.github.akotu235.shop.service.shop.validator.NewProductFormValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class SellerController {

    private final ShopService shopService;
    private final NewCategoryFormValidator newCategoryFormValidator;
    private final NewProductFormValidator newProductFormValidator;

    public SellerController(ShopService shopService, NewCategoryFormValidator newCategoryFormValidator, NewProductFormValidator newProductFormValidator) {
        this.shopService = shopService;
        this.newCategoryFormValidator = newCategoryFormValidator;
        this.newProductFormValidator = newProductFormValidator;
    }

    @GetMapping("/seller-panel")
    public ModelAndView getSellerPanel() {
        return new ModelAndView("seller-panel");
    }

    @GetMapping("/seller-panel/new-category")
    public ModelAndView newCategory(Model model) {
        model.addAttribute("category", new NewCategoryWriteModel());
        return new ModelAndView("new-category-form");
    }

    @PostMapping("/seller-panel/save-category")
    public ModelAndView saveCategory(@ModelAttribute("category") @Valid NewCategoryWriteModel category,
                                     BindingResult bindingResult,
                                     Model model) {
        newCategoryFormValidator.validate(category, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("new-category-form");
        }
        Result<CategoryReadModel> result = shopService.createCategory(category);
        model.addAttribute("result", result);
        return new ModelAndView("result");
    }

    @GetMapping("/seller-panel/new-product")
    public ModelAndView newProduct(Model model) {
        model.addAttribute("product", new NewProductWriteModel());
        return new ModelAndView("new-product-form");
    }

    @PostMapping("/seller-panel/save-product")
    public ModelAndView saveProduct(@ModelAttribute("product") @Valid NewProductWriteModel product,
                                    BindingResult bindingResult,
                                    Model model) {
        newProductFormValidator.validate(product, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("new-product-form");
        }
        Result<ProductReadModel> result = shopService.createProduct(product);
        model.addAttribute("result", result);
        return new ModelAndView("result");
    }

    @GetMapping("/seller-panel/disable-product/{productId}")
    public ModelAndView disableProduct(@PathVariable String productId, Model model) {
        model.addAttribute("result", shopService.disableProduct(productId));
        return new ModelAndView("result");
    }

    @GetMapping("/seller-panel/enable-product/{productId}")
    public ModelAndView enableProduct(@PathVariable String productId, Model model) {
        model.addAttribute("result", shopService.enableProduct(productId));
        return new ModelAndView("result");
    }

    @GetMapping("/seller-panel/orders")
    public ModelAndView getAllOrders(Model model) {
        model.addAttribute("orders", shopService.getAllOrders());
        return new ModelAndView("orders");
    }

    @PostMapping("/seller-panel/orders")
    public ModelAndView getOrders(@RequestParam("status") String status,
                                  Model model) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        model.addAttribute("orders", shopService.getOrders(orderStatus));
        return new ModelAndView("orders");
    }

    @GetMapping("/seller-panel/orders/{orderId}")
    public ModelAndView getOrder(@PathVariable String orderId, Model model) {
        model.addAttribute("order", shopService.getOrderById(orderId));
        List<OrderStatus> statuses = Arrays.stream(OrderStatus.values())
                .filter(status -> status != OrderStatus.PENDING && status != OrderStatus.PROCESSING)
                .toList();
        model.addAttribute("statuses", statuses);
        return new ModelAndView("order-details");
    }

    @PostMapping("/seller-panel/orders/{orderId}/updateStatus")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam("status") OrderStatus status) {
        shopService.updateStatus(orderId, status);
        return "redirect:/seller-panel/orders/" + orderId;
    }
}
