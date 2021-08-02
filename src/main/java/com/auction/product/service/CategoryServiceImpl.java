package com.auction.product.service;

import com.auction.product.exception.CategoryError;
import com.auction.product.exception.CategoryException;
import com.auction.product.exception.SubjectError;
import com.auction.product.exception.SubjectException;
import com.auction.product.model.Category;
import com.auction.product.model.CategoryDto;
import com.auction.product.model.Subject;
import com.auction.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryException(CategoryError.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryException(CategoryError.CATEGORY_ALREADY_EXISTS);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryException(CategoryError.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}
