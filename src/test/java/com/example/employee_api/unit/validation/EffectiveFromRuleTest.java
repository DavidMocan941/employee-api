package com.example.employee_api.unit.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.employee_api.common.exceptions.InvalidSalaryStartDateException;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.validation.rules.EffectiveFromRule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EffectiveFromRuleTest {
  private EffectiveFromRule effectiveFromRule;

  @BeforeEach
  void setUp() {
    effectiveFromRule = new EffectiveFromRule();
  }

  @Test
  void shouldThrowInvalidSalaryStartDateException_whenEffectiveFromIsInPast() {
    EmployeeCreateDTO dto = new EmployeeCreateDTO();
    dto.setEffectiveFrom(LocalDate.now().minusDays(1));
    assertThrows(InvalidSalaryStartDateException.class, () -> effectiveFromRule.validate(dto));
  }
}
