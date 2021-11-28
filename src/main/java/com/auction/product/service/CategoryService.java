package com.auction.product.service;

import com.auction.product.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getCategories();

    Category getCategory(String categoryName);

    Category addCategory(Category category);

    void deleteCategory(String categoryName);
}
