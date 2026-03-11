package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class DepartmentNotDeletedException extends BaseApiException {
    public DepartmentNotDeletedException(int id) {
        super(
                "Was not deleted the department with id: " + id + ". Exists employees in this department",
                HttpStatus.CONFLICT,
                "CONFLICT");
    }
}
