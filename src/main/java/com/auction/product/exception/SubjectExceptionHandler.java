package com.auction.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubjectExceptionHandler {
    @ExceptionHandler(value = SubjectException.class)
    public ResponseEntity<ErrorInfo> handleProductException(SubjectException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (SubjectError.SUBJECT_NOT_FOUND.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (SubjectError.SUBJECT_NOT_AVAILABLE.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (SubjectError.SUBJECT_ALREADY_EXISTS.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (SubjectError.PRICE_TOO_LOW.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (SubjectError.BIDDING_TIME_HAS_EXPIRED.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (SubjectError.MAX_SIZE_OF_FILE_EXCEEDED.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (SubjectError.DESCRIPTION_TOO_LONG.equals(e.getSubjectError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(httpStatus)
                .body(new ErrorInfo(e.getSubjectError().getMessage()));
    }

    @ExceptionHandler(value = CategoryException.class)
    public ResponseEntity<ErrorInfo> handleProductException(CategoryException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (CategoryError.CATEGORY_NOT_FOUND.equals(e.getCategoryError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (CategoryError.CATEGORY_ALREADY_EXISTS.equals(e.getCategoryError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(httpStatus)
                .body(new ErrorInfo(e.getCategoryError().getMessage()));
    }
}
