package com.example.employee_api.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeePatchDto {
  private String name;
  private String surname;
  private String email;
  private String departmentName;
  private String positionName;
  private LocalDate birth;
}
