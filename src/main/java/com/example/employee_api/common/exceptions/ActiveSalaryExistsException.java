package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class ActiveSalaryExistsException extends BaseApiException {
    public ActiveSalaryExistsException() {
        super("Exists an active salary",
                HttpStatus.CONFLICT,
                "CONFLICT");
    }
}
