package com.example.employee_api.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAndSalaryResponseDTO {
  private String name;
  private String surname;
  private String email;
  private LocalDateTime hireDate;
  private String department;
  private String position;
  private LocalDate birth;
  private BigDecimal salary;
  private String currency;
  private LocalDate effectiveFrom;
  private LocalDate effectiveTo;
}
