package com.example.employee_api.department.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class DepartmentSalaryBudgetDTO {
    private DepartmentResponseDTO departmentResponseDTO;
    private BigDecimal salaryBudget;
}
