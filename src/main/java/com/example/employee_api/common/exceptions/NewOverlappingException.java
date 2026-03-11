package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class NewOverlappingException extends BaseApiException {
    public NewOverlappingException() {
        super("Two salaries overlap",
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST"
        );
    }
}
