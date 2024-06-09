package com.redartis.auth.exception;

import org.springframework.http.HttpStatus;

public class TelegramAuthException extends BaseException {
    public TelegramAuthException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getErrorCode() {
        return "AUTH_TELEGRAM_VERIFY_FAILED";
    }
}
