package com.example.employee_api.validation.employee;

import com.example.employee_api.validation.Validator;
import com.example.employee_api.validation.context.EmployeeUpdateContext;
import com.example.employee_api.validation.rules.ExistingEmailRule;
import com.example.employee_api.validation.rules.ExistingEmployeeRule;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUpdatingValidator implements Validator<EmployeeUpdateContext> {
  private final ExistingEmailRule<EmployeeUpdateContext> existingEmailRule;
  private final ExistingEmployeeRule existingEmployeeRule;

  public EmployeeUpdatingValidator(
      ExistingEmailRule<EmployeeUpdateContext> existingEmailRule,
      ExistingEmployeeRule existingEmployeeRule) {
    this.existingEmployeeRule = existingEmployeeRule;
    this.existingEmailRule = existingEmailRule;
  }

  @Override
  public void validate(EmployeeUpdateContext context) {
    existingEmployeeRule.checkIfEmployeeExists(context);
    existingEmailRule.checkIfEmailPresent(context);
  }
}
