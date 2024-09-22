package io.github.akotu235.shop.service.shop.util;

import io.github.akotu235.shop.service.shop.entity.*;
import io.github.akotu235.shop.service.shop.projection.read.*;
import io.github.akotu235.shop.service.shop.projection.write.ShippingDetailsWriteModel;
import io.github.akotu235.shop.util.FormatUtils;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProjectionUtil {

    public static CategoryReadModel getCategoryReadModel(Category category) {
        return new CategoryReadModel(category.getId(), category.getName());
    }

    public static List<CategoryReadModel> getCategoriesReadModel(List<Category> categories) {
        return categories.stream().map(ProjectionUtil::getCategoryReadModel).toList();
    }

    public static ProductReadModel getProductReadModel(Product product) {
        return new ProductReadModel(product.getId(), product.getName(), product.getDescription(), FormatUtils.formatPrice(product.getPrice()), product.getCurrency(), product.getAvailableQuantity(), product.getPhotosQuantity(), product.isEnabled(), getCategoryReadModel(product.getCategory()));
    }

    public static Page<ProductReadModel> getProductsReadModel(Page<Product> products) {
        return products.map(ProjectionUtil::getProductReadModel);
    }

    public static OrderReadModel getOrderReadModel(Order order, double cartPrice, double totalPrice, String currency, DeliveryOptionReadModel deliveryOption, ShippingDetailsReadModel shippingDetails) {
        List<OrderPositionReadModel> positions = getOrderPositionsReadModel(order);
        return new OrderReadModel(order.getId(), order.getUser().getId(), order.getUser().getName() + " " + order.getUser().getSurname(), positions, order.getStatus(), deliveryOption, shippingDetails, FormatUtils.formatPrice(cartPrice), FormatUtils.formatPrice(totalPrice), currency, positions.size(), FormatUtils.formatDate(order.getSubmissionDate()));
    }

    public static List<OrderPositionReadModel> getOrderPositionsReadModel(Order order) {
        return order.getPositions().stream().map(orderPosition -> ProjectionUtil.getOrderPositionReadModel(orderPosition, orderPosition.getQuantity() * orderPosition.getProduct().getPrice())).toList();
    }

    public static CartReadModel getCartReadModel(Order order, List<CartPositionReadModel> positions, double cartPrice, String currency, boolean hasError) {
        return new CartReadModel(order.getId(), order.getUser().getId(), positions, order.getStatus(), FormatUtils.formatPrice(cartPrice), currency, positions.size(), hasError);
    }

    public static OrderPositionReadModel getOrderPositionReadModel(OrderPosition position, double totalPrice) {
        return new OrderPositionReadModel(position.getOrder().getId(), getProductReadModel(position.getProduct()), position.getQuantity(), FormatUtils.formatPrice(totalPrice));
    }

    public static ShippingDetailsReadModel getShippingDetailsReadModel(ShippingDetails shippingDetails) {
        if (shippingDetails == null) {
            return null;
        }
        return new ShippingDetailsReadModel(shippingDetails.getCountry(), shippingDetails.getStreet(), shippingDetails.getCity(), shippingDetails.getPostalCode(), shippingDetails.getPhone());
    }

    public static ShippingDetailsWriteModel getShippingDetailsWriteModel(ShippingDetailsReadModel shippingDetails) {
        return new ShippingDetailsWriteModel(shippingDetails.getCountry(), shippingDetails.getStreet(), shippingDetails.getCity(), shippingDetails.getPostalCode(), shippingDetails.getPhone());
    }
}