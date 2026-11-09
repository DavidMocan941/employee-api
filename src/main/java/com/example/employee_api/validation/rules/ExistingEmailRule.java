package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.validation.contract.EmailProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExistingEmailRule<T extends EmailProvider> {
  private final EmployeeRepository employeeRepository;
@Autowired
  public ExistingEmailRule(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public void checkIfEmailPresent(T t) {
    boolean result = employeeRepository.checkAnExistingEmail(t.getEmail());
    if (result) {
      throw new EmailAlreadyExistException(t.getEmail());
    }
  }
}
