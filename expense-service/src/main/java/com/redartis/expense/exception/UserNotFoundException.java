package com.redartis.expense.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getErrorCode() {
        return "ORCHESTRA_USER_NOT_FOUND";
    }
}
