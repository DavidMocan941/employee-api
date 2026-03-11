package com.example.employee_api.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SalaryCreateDTO {
    @NotNull(message = "Indicate the salary")
    @Positive
    private BigDecimal salary;
    @NotBlank(message = "Indicate the currency")
    private String currency;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    @NotNull(message = "Indicate the employee's id")
    private int employeeId;
}
