package com.example.employee_api.common.exceptions;

import org.springframework.http.HttpStatus;

public class SalaryPeriodConflictException extends BaseApiException {
    public SalaryPeriodConflictException() {
        super("Two salaries for an employee at the same period of time",
                HttpStatus.CONFLICT,
                "CONFLICT");
    }

}
