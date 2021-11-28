package com.auction.product.exception;

public enum CategoryError {
    CATEGORY_NOT_FOUND("Nie znaleziono kategorii"),
    CATEGORY_ALREADY_EXISTS("Kategoria już istnieje");

    private String message;

    CategoryError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
