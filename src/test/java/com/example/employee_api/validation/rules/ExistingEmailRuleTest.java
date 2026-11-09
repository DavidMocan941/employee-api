package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeeCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExistingEmailRuleTest {
  @Mock private EmployeeRepository employeeRepository;
  @InjectMocks private ExistingEmailRule<EmployeeCreateDto> existingEmailRule;

  @Test
  void checkExistingEmail_emailExists_throwException() {
    EmployeeCreateDto dto = new EmployeeCreateDto();
    dto.setEmail("user@gmail.com");
    when(employeeRepository.checkAnExistingEmail(dto.getEmail())).thenReturn(true);
    assertThrows(
        EmailAlreadyExistException.class, () -> existingEmailRule.checkIfEmailPresent(dto));
    verify(employeeRepository, times(1)).checkAnExistingEmail(dto.getEmail());
  }

  @Test
  void checkExistingEmail_newEmail_doesNotThrowException() {
    EmployeeCreateDto dto = new EmployeeCreateDto();
    dto.setEmail("user@gmail.com");
    when(employeeRepository.checkAnExistingEmail(dto.getEmail())).thenReturn(false);
    assertDoesNotThrow(() -> existingEmailRule.checkIfEmailPresent(dto));
    verify(employeeRepository).checkAnExistingEmail(dto.getEmail());
  }
}
