package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class SalaryNotFoundException extends BaseApiException {
    public SalaryNotFoundException(int id) {
        super("Not found the salary with id: " + id
                ,HttpStatus.NOT_FOUND,
                "NOT_FOUND");
    }
}
