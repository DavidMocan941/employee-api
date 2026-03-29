package com.example.employee_api.common.exceptions;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;

public class InvalidSalaryStartDateException extends BaseApiException {
  public InvalidSalaryStartDateException(LocalDate effectiveFrom) {
    super(
        "Was introduced an invalid salary date:" + effectiveFrom,
        HttpStatus.BAD_REQUEST,
        "BAD_REQUEST");
  }
}
