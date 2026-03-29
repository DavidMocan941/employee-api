package com.example.employee_api.employee.validation;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.validation.rules.RuleValidator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {
  private final List<RuleValidator<EmployeeCreateDTO>> rules;

  public EmployeeValidator(List<RuleValidator<EmployeeCreateDTO>> rules) {
    this.rules = rules;
  }

  public void validateWhenCreateEmployee(EmployeeCreateDTO createDTO) {
    rules.forEach((rule) -> rule.validate(createDTO));
  }
}
