package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmployeeNotFoundException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeePutDto;
import com.example.employee_api.validation.context.EmployeeUpdateContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExistingEmployeeRuleTest {
  @Mock private EmployeeRepository repository;
  @InjectMocks private ExistingEmployeeRule rule;

  @Test
  public void checkIfEmployeeExists_employeeDoesNotExist_throwsEmployeeNotFoundException() {
    EmployeeUpdateContext context = new EmployeeUpdateContext(1, null);
    when(repository.checkIfEmployeeExists(1)).thenReturn(false);

    assertThrows(EmployeeNotFoundException.class, () -> rule.checkIfEmployeeExists(context.id()));

    verify(repository).checkIfEmployeeExists(1);
  }

  @Test
  public void checkIfEmployeeExists_employeeExists_theRuleIsSuccessfullyValidated() {
    EmployeeUpdateContext context = new EmployeeUpdateContext(1, null);
    when(repository.checkIfEmployeeExists(1)).thenReturn(true);

    assertDoesNotThrow(() -> rule.checkIfEmployeeExists(context.id()));

    verify(repository).checkIfEmployeeExists(1);
  }
}
