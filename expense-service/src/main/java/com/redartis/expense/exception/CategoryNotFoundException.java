package com.redartis.expense.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException() {
        super();
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getErrorCode() {
        return "ORCHESTRA_CATEGORY_NOT_FOUND";
    }
}
