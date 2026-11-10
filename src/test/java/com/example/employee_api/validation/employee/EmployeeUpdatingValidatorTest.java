package com.example.employee_api.validation.employee;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.common.exceptions.EmployeeNotFoundException;
import com.example.employee_api.validation.context.EmployeeUpdateContext;
import com.example.employee_api.validation.rules.ExistingEmailRule;
import com.example.employee_api.validation.rules.ExistingEmployeeRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeUpdatingValidatorTest {
  @Mock private ExistingEmployeeRule existingEmployeeRule;
  @Mock private ExistingEmailRule<EmployeeUpdateContext> existingEmailRule;
  @InjectMocks private EmployeeUpdatingValidator validator;

  @Test
  public void validate_checkIfEmployeeExistsThrowsException_everythingElseIsNotExecuted() {
    EmployeeUpdateContext context = new EmployeeUpdateContext(1, null);

    doThrow(new EmployeeNotFoundException(0)).when(existingEmployeeRule).checkIfEmployeeExists(1);

    assertThrows(EmployeeNotFoundException.class, () -> validator.validate(context));

    verify(existingEmployeeRule).checkIfEmployeeExists(1);
    verify(existingEmailRule, never()).checkIfEmailPresent(context);
  }

  @Test
  public void validate_checkIfEmailPresentThrowsException_validationDoesNotPass() {
    EmployeeUpdateContext context = new EmployeeUpdateContext(1, null);

    doThrow(new EmailAlreadyExistException(null))
        .when(existingEmailRule)
        .checkIfEmailPresent(context);

    assertThrows(EmailAlreadyExistException.class, () -> validator.validate(context));

    InOrder inOrder = inOrder(existingEmployeeRule, existingEmailRule);
    inOrder.verify(existingEmployeeRule).checkIfEmployeeExists(1);
    inOrder.verify(existingEmailRule).checkIfEmailPresent(context);
  }

  @Test
  public void validate_noExceptionIsThrown_validationPasses() {
    EmployeeUpdateContext context = new EmployeeUpdateContext(1, null);

    assertDoesNotThrow(() -> validator.validate(context));

    InOrder inOrder = inOrder(existingEmployeeRule, existingEmailRule);

    inOrder.verify(existingEmployeeRule).checkIfEmployeeExists(1);
    inOrder.verify(existingEmailRule).checkIfEmailPresent(context);
  }
}
