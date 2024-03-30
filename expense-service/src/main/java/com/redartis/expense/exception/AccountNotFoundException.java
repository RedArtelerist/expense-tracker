package com.redartis.expense.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getErrorCode() {
        return "ORCHESTRA_ACCOUNT_NOT_FOUND";
    }
}
