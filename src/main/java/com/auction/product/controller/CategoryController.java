package com.auction.product.controller;

import com.auction.product.model.Category;
import com.auction.product.model.Subject;
import com.auction.product.service.CategoryService;
import com.auction.product.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final SubjectService subjectService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/{categoryName}")
    public void deleteCategory(@PathVariable String categoryName) {
        Category category = categoryService.getCategory(categoryName);
        List<Subject> subjects = subjectService.getSubjectsByCategory(category);
        subjects.forEach(subject -> {
            subject.removeCategory(category);
        });

        categoryService.deleteCategory(categoryName);
    }
}
