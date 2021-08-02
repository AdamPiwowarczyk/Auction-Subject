package com.auction.product.exception;

public enum SubjectError {//Będzie trzeba zamienić na polskie
    SUBJECT_NOT_FOUND("Subject not found"),
    SUBJECT_NOT_AVAILABLE("Subject not available"),
    SUBJECT_ALREADY_EXISTS("Subject already exists"),
    PRICE_TOO_LOW("Too low price"),
    BIDDING_TIME_HAS_EXPIRED("The bidding time has expired"),
    MAX_SIZE_OF_FILE_EXCEEDED("Max size for picture file is 16 MB"),
    DESCRIPTION_TOO_LONG("Max length of description is 500 characters");

    private String message;

    SubjectError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
