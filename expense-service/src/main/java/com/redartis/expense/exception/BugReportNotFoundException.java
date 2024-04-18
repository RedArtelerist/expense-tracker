package com.redartis.expense.exception;

import org.springframework.http.HttpStatus;

public class BugReportNotFoundException extends BaseException {
    public BugReportNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getErrorCode() {
        return "ORCHESTRA_BUG_REPORT_NOT_FOUND";
    }
}
