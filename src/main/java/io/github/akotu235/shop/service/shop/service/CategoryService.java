package io.github.akotu235.shop.service.shop.service;

import io.github.akotu235.shop.exceptions.InvalidAddressException;
import io.github.akotu235.shop.service.shop.entity.Category;
import io.github.akotu235.shop.service.shop.projection.write.NewCategoryWriteModel;
import io.github.akotu235.shop.service.shop.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(@Valid NewCategoryWriteModel newCategory) {
        return categoryRepository.save(new Category(newCategory));
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            return categoryRepository.findByName(name).get();
        }
        throw new InvalidAddressException("error.category.not-exist", name);
    }
}