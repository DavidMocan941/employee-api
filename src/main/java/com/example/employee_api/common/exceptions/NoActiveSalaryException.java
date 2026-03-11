package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoActiveSalaryException extends BaseApiException {
    public NoActiveSalaryException() {
        super("There is not active salary for this employee",
                HttpStatus.NOT_FOUND,
                "NOT_FOUND");
    }
}
