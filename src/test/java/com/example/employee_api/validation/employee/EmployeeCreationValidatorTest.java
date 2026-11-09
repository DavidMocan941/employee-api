package com.example.employee_api.validation.employee;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.common.exceptions.InvalidDataRangeException;
import com.example.employee_api.employee.dto.EmployeeCreateDto;
import com.example.employee_api.validation.rules.DateRangeRule;
import com.example.employee_api.validation.rules.ExistingEmailRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeCreationValidatorTest {
  @Mock private DateRangeRule dateRangeRule;
  @Mock private ExistingEmailRule<EmployeeCreateDto> existingEmailRule;
  @InjectMocks private EmployeeCreationValidator validator;

  @Test
  void validate_validEmployee_aValidEmployeePassesValidation() {
    EmployeeCreateDto dto = new EmployeeCreateDto();
    dto.setEffectiveTo(LocalDate.of(2000, 1, 2));
    dto.setEffectiveFrom(null);
    dto.setEmail("user@gmail.com");

    LocalDate from = LocalDate.of(2000, 1, 1);

    when(dateRangeRule.checkDateRange(null, dto.getEffectiveTo())).thenReturn(from);

    validator.validate(dto);

    assertEquals(from, dto.getEffectiveFrom());

    verify(dateRangeRule).checkDateRange(null, LocalDate.of(2000, 1, 2));
    verify(existingEmailRule).checkIfEmailPresent(dto);
  }

  @Test
  void validate_invalidDateRange_doesNotExecuteAnythingElseAfterException() {
    EmployeeCreateDto dto = new EmployeeCreateDto();
    LocalDate from = LocalDate.of(2000, 1, 2);
    LocalDate to = LocalDate.of(2000, 1, 1);
    dto.setEffectiveFrom(from);
    dto.setEffectiveTo(to);

    when(dateRangeRule.checkDateRange(from, to)).thenThrow(new InvalidDataRangeException());

    assertThrows(InvalidDataRangeException.class, () -> validator.validate(dto));

    verify(dateRangeRule).checkDateRange(LocalDate.of(2000, 1, 2), LocalDate.of(2000, 1, 1));
    verify(existingEmailRule, never()).checkIfEmailPresent(any());
  }

  @Test
  void validate_emailAlreadyExists_doesNotExecuteAnythingElseAfterException() {
    EmployeeCreateDto dto = new EmployeeCreateDto();
    LocalDate from = LocalDate.of(2000, 1, 2);
    LocalDate to = LocalDate.of(2000, 1, 3);
    dto.setEffectiveFrom(null);
    dto.setEffectiveTo(to);
    dto.setEmail("user@gmail.com");

    when(dateRangeRule.checkDateRange(null, to)).thenReturn(from);
    doThrow(new EmailAlreadyExistException("Email already exists"))
        .when(existingEmailRule)
        .checkIfEmailPresent(dto);

    assertThrows(EmailAlreadyExistException.class, () -> validator.validate(dto));
    assertEquals(from, dto.getEffectiveFrom());

    verify(dateRangeRule).checkDateRange(null, to);
    verify(existingEmailRule).checkIfEmailPresent(dto);
  }
}
