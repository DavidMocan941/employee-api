package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException extends BaseApiException {
    public DepartmentNotFoundException(Object id) {
        super("Department not found:" + id,
                HttpStatus.NOT_FOUND,
                "NOT FOUND");
    }
}
