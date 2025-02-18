package io.github.akotu235.shop;

import io.github.akotu235.shop.properties.AppConfigurationProperties;
import io.github.akotu235.shop.service.shop.ShopService;
import io.github.akotu235.shop.service.shop.entity.Category;
import io.github.akotu235.shop.service.shop.entity.Product;
import io.github.akotu235.shop.service.shop.projection.write.NewCategoryWriteModel;
import io.github.akotu235.shop.service.shop.properties.ShopConfigurationProperties;
import io.github.akotu235.shop.service.shop.repository.CategoryRepository;
import io.github.akotu235.shop.service.shop.repository.ProductRepository;
import io.github.akotu235.shop.service.user.UserService;
import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.service.user.model.UserRole;
import io.github.akotu235.shop.service.user.projection.UserWriteModel;
import io.github.akotu235.shop.service.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@Component
@ConditionalOnProperty(name = "app.load-sample-data", havingValue = "true")
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopService shopService;
    private final ShopConfigurationProperties shopConfig;
    private final AppConfigurationProperties appConfig;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(UserService userService, UserRepository userRepository, ProductRepository productRepository, CategoryRepository categoryRepository, ShopService shopService, ShopConfigurationProperties shopConfig, AppConfigurationProperties appConfig) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.shopService = shopService;
        this.shopConfig = shopConfig;
        this.appConfig = appConfig;
    }

    @Override
    public void run(String... args) {
        //Users
        addUser("user", "Jan", "Kowalski", "user@example.com", "user", false);
        addUser("seller", "Jan", "Kowalski", "seller@example.com", "seller", true);

        //Categories
        addCategory("Candy bars");
        addCategory("Chocolate");
        addCategory("Jelly candy");

        //Products
        addProduct("Candy bars", "Nougat bar", 4.19, 234);
        addProduct("Candy bars", "Caramel bar", 4.99, 435);
        addProduct("Candy bars", "Peanut bar", 5.99, 3032);
        addProduct("Candy bars", "Wafer bar", 3.99, 234);
        addProduct("Candy bars", "Granola bar", 2.99, 532);

        addProduct("Chocolate", "Dark chocolate", 2.29, 23);
        addProduct("Chocolate", "Milk chocolate", 5.19, 2412);
        addProduct("Chocolate", "White chocolate", 5.19, 1802);
        addProduct("Chocolate", "Chocolate truffle", 6.99, 614);
        addProduct("Chocolate", "Chocolate praline", 5.99, 614);

        addProduct("Jelly candy", "Gummy bears", 5., 632);
        addProduct("Jelly candy", "Jelly beans", 5., 458);
        addProduct("Jelly candy", "Fruit jellies", 5., 289);
        addProduct("Jelly candy", "Sour gummies", 5., 987);
        addProduct("Jelly candy", "Wine gums", 5., 785);
    }

    private void addUser(String username, String name, String surname, String email, String password, boolean isSeller) {
        if (userRepository.findByUsername(username) == null) {
            UserWriteModel newUser = new UserWriteModel();
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setConfirmPassword(password);
            newUser.setLocale(Locale.ENGLISH);
            User user = userService.createUser(newUser).object();
            if (isSeller) {
                user.getRoles().add(new UserRole(user, "SELLER"));
            }
            user.setEnabled(true);

            userRepository.save(user);
            logger.info("Created new user: {}", newUser.getUsername());
        }
    }

    private void addCategory(String name) {
        if (!categoryRepository.existsByName(name)) {
            NewCategoryWriteModel category = new NewCategoryWriteModel();
            category.setName(name);

            shopService.createCategory(category);
            logger.info("Created new category: {}", category.getName());
        }
    }

    private void addProduct(String categoryName, String name, double price, int quantity) {
        Category category = shopService.getCategory(categoryName);
        if (productRepository.findByNameAndCategory(name, category).isEmpty()) {
            Product product = new Product();
            product.setName(name);
            product.setCategory(shopService.getCategory(category.getName()));
            product.setPrice(BigDecimal.valueOf(price));
            product.setCurrency(shopConfig.getCurrency());
            product.setAvailableQuantity(quantity);
            product.setDescription("Delicious Sweets");
            product.setEnabled(true);

            long productId = productRepository.save(product).getId();
//            copyProductImage(productId, name);
            logger.info("Created new product: {}", product.getName());
        }
    }

    private void copyProductImage(long id, String name) {
        InputStream resourceStream = getClass().getResourceAsStream("/samples/products/" + name + ".jpg");
        if (resourceStream == null) {
            logger.error("The {} picture was not found", name);
        } else {
            Path directoryPath = Paths.get(appConfig.getDataPath() + File.separator + "products" + File.separator + id);
            Path destinationPath = Paths.get(directoryPath.toString(), "1.jpg");
            try {
                Files.createDirectories(directoryPath);
                Files.copy(resourceStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.error("{} image copy error", name);
            }
        }
    }
}