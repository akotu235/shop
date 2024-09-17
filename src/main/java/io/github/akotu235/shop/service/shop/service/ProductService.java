package io.github.akotu235.shop.service.shop.service;

import io.github.akotu235.shop.exceptions.AppException;
import io.github.akotu235.shop.exceptions.InvalidAddressException;
import io.github.akotu235.shop.service.files.FileService;
import io.github.akotu235.shop.service.shop.entity.Category;
import io.github.akotu235.shop.service.shop.entity.OrderPosition;
import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.projection.read.OrderPositionReadModel;
import io.github.akotu235.shop.service.shop.projection.write.NewProductWriteModel;
import io.github.akotu235.shop.service.shop.properties.ShopConfigurationProperties;
import io.github.akotu235.shop.service.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final ShopConfigurationProperties config;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, FileService fileService, ShopConfigurationProperties config) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.fileService = fileService;
        this.config = config;
    }

    @Transactional(rollbackOn = AppException.class)
    public Product createProduct(@Valid NewProductWriteModel newProduct) {
        newProduct.setCurrency(config.getCurrency());
        Category category = categoryService.getCategory(newProduct.getCategory());
        Product product = productRepository.save(new Product(newProduct, category));
        String path = "products" + File.separator + product.getId() + File.separator;
        fileService.saveImageAsJpg(newProduct.getPhoto(), path, "1.jpg");
        return product;
    }

    public Page<Product> getAllProducts(Pageable page) {
        return productRepository.findByEnabledTrueAndAvailableQuantityGreaterThan(0, page);
    }

    public Page<Product> getProductsByCategoryName(Pageable page, String categoryName) {
        Category category = categoryService.getCategory(categoryName);
        return productRepository.findByEnabledTrueAndAvailableQuantityGreaterThanAndCategoryId(0, category.getId(), page);
    }

    public Product getProduct(String productId) {
        Long id = Long.parseLong(productId);
        if (productRepository.findProductById(id).isPresent()) {
            return productRepository.findProductById(id).get();
        }
        throw new InvalidAddressException("error.product.not-exist", productId);
    }

    public ResponseEntity<InputStreamResource> getProductPhoto(Long productId, Long photoId) {
        return fileService.getImage("products" + File.separator + productId + File.separator + photoId + ".jpg");
    }

    Product getProduct(Long id) {
        if (productRepository.findProductById(id).isPresent()) {
            return productRepository.findProductById(id).get();
        }
        throw new InvalidAddressException("error.product.not-exist", String.valueOf(id));
    }

    public Product setEnableProduct(Long productId, boolean enable) {
        Optional<Product> productOptional = productRepository.findProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setEnabled(enable);
            return productRepository.save(product);
        } else throw new InvalidAddressException("error.product.not-exist", String.valueOf(productId));
    }

    public Page<Product> searchProductsByName(Pageable page, String name) {
        return productRepository.findByNameOrCategoryNameContainingIgnoreCase(page, name);
    }

    @Transactional(rollbackOn = AppException.class)
    public void reserveProducts(List<OrderPositionReadModel> positions) {
        for (OrderPositionReadModel position : positions) {
            Product product = getProduct(position.getProduct().getId());
            product.setAvailableQuantity(product.getAvailableQuantity() - position.getQuantity());
            productRepository.save(product);
        }
    }

    @Transactional(rollbackOn = AppException.class)
    public void cancelProductReservations(List<OrderPosition> positions) {
        for (OrderPosition position : positions) {
            Product product = position.getProduct();
            product.setAvailableQuantity(product.getAvailableQuantity() + position.getQuantity());
            productRepository.save(product);
        }
    }
}