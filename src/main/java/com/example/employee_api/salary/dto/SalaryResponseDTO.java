package com.example.employee_api.salary.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SalaryResponseDTO {
    private int id;
    private BigDecimal salary;
    private String currency;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private int employeeId;
}
