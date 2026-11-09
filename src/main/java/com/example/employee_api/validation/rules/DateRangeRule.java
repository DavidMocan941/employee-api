package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.InvalidDataRangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
public class DateRangeRule {

  public LocalDate checkDateRange(LocalDate effectiveFrom, LocalDate effectiveTo) {
    LocalDate actualEffectiveFrom = Optional.ofNullable(effectiveFrom).orElse(LocalDate.now());

    if (effectiveTo != null && effectiveTo.isBefore(actualEffectiveFrom)) {
      throw new InvalidDataRangeException();
    }
    return actualEffectiveFrom;
  }
}
