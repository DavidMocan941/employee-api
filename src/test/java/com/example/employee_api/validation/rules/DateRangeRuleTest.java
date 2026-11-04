package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.InvalidDataRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateRangeRuleTest {
    private final DateRangeRule dateRangeRule = new DateRangeRule();

    @Test
    void checkDateRange_nullEffectiveFromPastEffectiveTo_throwsException() {
        LocalDate effectiveFrom = null;
        LocalDate effectiveTo = LocalDate.now().minusDays(1);
        assertThrows(InvalidDataRangeException.class,
                () -> dateRangeRule.checkDateRange(effectiveFrom, effectiveTo));
    }

    @Test
    void checkDateRange_nullEffectiveFromFutureEffectiveTo_notThrowException() {
        LocalDate effectiveFrom = null;
        LocalDate effectiveTo = LocalDate.now().plusDays(1);
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(effectiveFrom, effectiveTo));

    }

    @Test
    void checkDateRange_assignedEffectiveFromPastEffectiveTo_throwException() {
        LocalDate effectiveFrom = LocalDate.of(2000, 1, 2);
        LocalDate effectiveTo = LocalDate.of(2000, 1, 1);
        assertThrows(InvalidDataRangeException.class, () -> dateRangeRule.checkDateRange(effectiveFrom, effectiveTo));
    }

    @Test
    void checkDateRange_assignedEffectiveFromBeforeEffectiveTo_notThrowException() {
        LocalDate effectiveFrom = LocalDate.of(2000, 1, 1);
        LocalDate effectiveTo = LocalDate.of(2000, 1, 2);
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(effectiveFrom, effectiveTo));
    }

    @Test
    void checkDateRange_assignedEffectiveFromUnassignedEffectiveTo_notThrowException() {
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(LocalDate.of(2000, 1, 1),
                null));
    }

    @Test
    void checkDateRange_UnassignedEffectiveFromUnassignedEffectiveTo_notThrowException() {
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(null, null));
    }

    @Test
    void checkDateRange_EffectiveFromAndEffectiveToAreEqual_notThrowException() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(date, date));
    }

    @Test
    void checkDateRange_UnassignedEffectiveFromAndEffectiveToIsToday_notThrowException() {
        assertDoesNotThrow(() -> dateRangeRule.checkDateRange(null, LocalDate.now()));
    }
}
