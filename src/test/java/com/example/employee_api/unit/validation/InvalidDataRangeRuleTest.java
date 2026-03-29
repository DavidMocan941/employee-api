package com.example.employee_api.unit.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.employee_api.common.exceptions.InvalidDataRangeException;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.validation.rules.DataRangeRule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InvalidDataRangeRuleTest {
  private DataRangeRule dataRangeRule;

  @BeforeEach
  void setUp() {
    dataRangeRule = new DataRangeRule();
  }

  @Test
  void shouldThrowInvalidDataRangeException_whenEffectiveFromIsBeforeEffectiveTo() {
    EmployeeCreateDTO dto = new EmployeeCreateDTO();
    dto.setEffectiveFrom(LocalDate.now());
    dto.setEffectiveTo(LocalDate.now().minusDays(1));
    assertThrows(InvalidDataRangeException.class, () -> dataRangeRule.validate(dto));
  }
}
