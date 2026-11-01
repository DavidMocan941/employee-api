package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExistingEmailRule {
    EmployeeRepository employeeRepository;

    public void checkIfEmailPresent(String email) {
        boolean result = employeeRepository.checkAnExistingEmail(email);
        if (result) {
            log.warn("Employee creation failed - email already exists: {}", email);
            throw new EmailAlreadyExistException(email);
        }
    }
}
