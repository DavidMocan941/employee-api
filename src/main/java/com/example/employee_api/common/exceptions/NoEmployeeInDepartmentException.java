package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoEmployeeInDepartmentException extends BaseApiException {
    public NoEmployeeInDepartmentException(int departmentId) {
        super("There is no employee in department with id: " + departmentId,
                HttpStatus.NOT_FOUND,
                "NOT_FOUND");
    }
}
