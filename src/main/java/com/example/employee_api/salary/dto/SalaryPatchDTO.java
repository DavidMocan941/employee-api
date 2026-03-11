package com.example.employee_api.salary.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Setter
@Getter
public class SalaryPatchDTO {
    private BigDecimal salary;
    private LocalDate effectiveTo;
}
