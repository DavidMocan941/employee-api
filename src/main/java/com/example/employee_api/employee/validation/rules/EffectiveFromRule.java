package com.example.employee_api.employee.validation.rules;

import com.example.employee_api.common.exceptions.InvalidSalaryStartDateException;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class EffectiveFromRule implements RuleValidator<EmployeeCreateDTO> {
  @Override
  public void validate(EmployeeCreateDTO employeeCreateDTO) {
    LocalDate effectiveFrom = employeeCreateDTO.getEffectiveFrom();
    if (effectiveFrom.isBefore(LocalDate.now()))
      throw new InvalidSalaryStartDateException(effectiveFrom);
  }
}
