package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class PositionNotFoundException extends BaseApiException {
    public PositionNotFoundException(String position) {
        super("Position not found:" + position,
                HttpStatus.NOT_FOUND,
                "NOT FOUND");
    }
}
