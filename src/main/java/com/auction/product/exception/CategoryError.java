package com.auction.product.exception;

public enum CategoryError {
    CATEGORY_NOT_FOUND("Category not found"),
    CATEGORY_ALREADY_EXISTS("Category already exists");

    private String message;

    CategoryError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
