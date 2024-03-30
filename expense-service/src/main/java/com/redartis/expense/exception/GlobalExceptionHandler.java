package com.redartis.expense.exception;

import com.redartis.dto.CommonErrorDto;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_CODE = "ORCHESTRA_UNEXPECTED";
    private static final int INTERNAL_SERVER_STATUS_CODE = 500;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxSizeRequest;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> test(MaxUploadSizeExceededException e) {
        return handleException(new RequestSizeException(
                "The file size must not exceed " + maxSizeRequest)
        );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        String errorCode = getErrorCode(e);
        int statusCode = getHttpStatusCode(e);
        return ResponseEntity.status(statusCode).body(createDto(errorCode, e.getMessage()));
    }

    private String getErrorCode(Exception exception) {
        if (exception instanceof BaseException) {
            return ((BaseException) exception).getErrorCode();
        }
        return INTERNAL_SERVER_ERROR_CODE;
    }

    private int getHttpStatusCode(Exception exception) {
        if (exception instanceof BaseException) {
            return ((BaseException) exception).getStatusCode();
        }
        return INTERNAL_SERVER_STATUS_CODE;
    }

    private CommonErrorDto createDto(String errorCode, String errorMessage) {
        return CommonErrorDto
                .builder()
                .code(errorCode)
                .message(errorMessage)
                .timestamp(Instant.now())
                .build();
    }
}
