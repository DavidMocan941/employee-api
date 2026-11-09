package com.example.employee_api.validation.context;

import com.example.employee_api.employee.dto.EmployeePutDto;
import com.example.employee_api.validation.contract.EmailProvider;

public record EmployeeUpdateContext(int id, EmployeePutDto dto) implements EmailProvider {
  @Override
  public String getEmail() {
    return dto.getEmail();
  }
}
