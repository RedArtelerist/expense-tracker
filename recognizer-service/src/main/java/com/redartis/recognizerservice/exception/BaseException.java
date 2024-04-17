package com.redartis.recognizerservice.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable exception) {
        super(message, exception);
    }

    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public String getErrorCode() {
        return "RECOGNIZER_SERVICE_UNEXPECTED";
    }
}
