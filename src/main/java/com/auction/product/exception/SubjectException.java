package com.auction.product.exception;

public class SubjectException extends RuntimeException {
    private SubjectError subjectError;

    public SubjectException(SubjectError subjectError) {
        this.subjectError = subjectError;
    }

    public SubjectError getSubjectError() {
        return subjectError;
    }
}
