package com.example.employee_api.employee.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EmployeePutDto{
  @NotBlank(message = "Required field")
  private String name;

  @NotBlank(message = "Required field")
  private String surname;

  @NotBlank(message = "Required field")
  @Email(message = "Must be a valid email")
  private String email;

  @NotBlank(message = "Required field")
  private String departmentName;

  @NotBlank(message = "Required field")
  private String positionName;

  @NotNull(message = "Required field")
  @Past(message = "Past date required")
  private LocalDate birth;
}
