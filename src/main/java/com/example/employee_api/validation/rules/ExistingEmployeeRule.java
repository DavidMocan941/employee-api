package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmployeeNotFoundException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.validation.context.EmployeeUpdateContext;
import org.springframework.stereotype.Component;

@Component
public class ExistingEmployeeRule {
  private final EmployeeRepository repository;

  public ExistingEmployeeRule(EmployeeRepository repository) {
    this.repository = repository;
  }

  public void checkIfEmployeeExists(EmployeeUpdateContext context) {
    boolean empExists = repository.checkIfEmployeeExists(context.id());
    if (!empExists) {
      throw new EmployeeNotFoundException(context.id());
    }
  }
}
