package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataRangeException extends BaseApiException {
    public InvalidDataRangeException() {
        super("The end of the salary is before the start",
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST");
    }

}
