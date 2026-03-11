package com.example.employee_api.salary.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class Salary {
    private int id;
    private BigDecimal salary;
    private String currency;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private int employeeId;
    private int createdBy;
    private LocalDateTime createdAt;
    private int updatedBy;
    private LocalDateTime updatedAt;
}
