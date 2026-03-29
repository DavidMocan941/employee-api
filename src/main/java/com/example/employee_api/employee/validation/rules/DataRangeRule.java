package com.example.employee_api.employee.validation.rules;

import com.example.employee_api.common.exceptions.InvalidDataRangeException;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class DataRangeRule implements RuleValidator<EmployeeCreateDTO> {
  @Override
  public void validate(EmployeeCreateDTO employeeCreateDTO) {
    if (employeeCreateDTO.getEffectiveTo() != null
        && employeeCreateDTO.getEffectiveTo().isBefore(employeeCreateDTO.getEffectiveFrom())) {
      throw new InvalidDataRangeException();
    }
  }
}
