package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends BaseApiException {
    public EmailAlreadyExistException(String email) {
        super("Already exist an employee with this email: " + email,
                HttpStatus.CONFLICT,
                "CONFLICT");
    }
}
