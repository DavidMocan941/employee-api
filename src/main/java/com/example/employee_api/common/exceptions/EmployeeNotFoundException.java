package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends BaseApiException {
    public EmployeeNotFoundException(int id) {
        super("Employee not found with id: " + id,
                HttpStatus.NOT_FOUND,
                "NOT_FOUND");
    }
}
