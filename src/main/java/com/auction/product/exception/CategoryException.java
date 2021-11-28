package com.auction.product.exception;

public class CategoryException extends RuntimeException {
    private final CategoryError categoryError;

    public CategoryException(CategoryError categoryError) {
        this.categoryError = categoryError;
    }

    public CategoryError getCategoryError() {
        return categoryError;
    }
}
