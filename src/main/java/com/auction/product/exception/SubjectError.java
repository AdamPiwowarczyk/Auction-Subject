package com.auction.product.exception;

public enum SubjectError {
    SUBJECT_NOT_FOUND("Nie znaleziono przedmiotu"),
    SUBJECT_NOT_AVAILABLE("Przedmiot niedostępny"),
    SUBJECT_ALREADY_EXISTS("Taki przedmiot już istnieje"),
    PRICE_TOO_LOW("Za niska cena"),
    BIDDING_TIME_HAS_EXPIRED("Termin licytacji minął"),
    MAX_SIZE_OF_FILE_EXCEEDED("Maksymalny rozmiar pliku to 16 MB"),
    DESCRIPTION_TOO_LONG("Maksymalna długość opisu to 500 znaków");

    private String message;

    SubjectError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
