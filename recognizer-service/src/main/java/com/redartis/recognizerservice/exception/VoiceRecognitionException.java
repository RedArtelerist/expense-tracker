package com.redartis.recognizerservice.exception;

import org.springframework.http.HttpStatus;

public class VoiceRecognitionException extends BaseException {
    public VoiceRecognitionException() {
        super();
    }

    public VoiceRecognitionException(String message) {
        super(message);
    }

    public VoiceRecognitionException(String message, Throwable exception) {
        super(message, exception);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getErrorCode() {
        return "ORCHESTRA_ACCOUNT_NOT_FOUND";
    }
}
