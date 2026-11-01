package com.example.employee_api.validation.employee;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.validation.Validator;
import com.example.employee_api.validation.rules.DateRangeRule;
import com.example.employee_api.validation.rules.ExistingEmailRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmployeeCreationValidator implements Validator<EmployeeCreateDTO> {
    private final DateRangeRule dateRangeRule;
    private final ExistingEmailRule existingEmailRule;


    @Override
    public void validate(EmployeeCreateDTO employeeCreateDTO) {
        LocalDate effectiveFrom = dateRangeRule.checkDateRange(employeeCreateDTO.getEffectiveFrom(),
                employeeCreateDTO.getEffectiveTo());
        employeeCreateDTO.setEffectiveFrom(effectiveFrom);
        existingEmailRule.checkIfEmailPresent(employeeCreateDTO.getEmail());
    }
}
