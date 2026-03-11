package com.example.employee_api.analytics.service;

import com.example.employee_api.ErrorCodes.ErrorCode;
import com.example.employee_api.common.EntityValidator;
import com.example.employee_api.common.Validator;
import com.example.employee_api.common.exceptions.DepartmentNotFoundException;
import com.example.employee_api.common.exceptions.EmployeeNotFoundException;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.salary.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class EmployeeAnalyticsService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final Validator entityValidator;

    public BigDecimal findEmployeeAvgSalary(int id) {
        entityValidator.validateExists(employeeRepository.checkIfEmployeeExists(id),
                () -> new EmployeeNotFoundException(id));
        return salaryRepository.getAverageSalaryById(id).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal findEmployeeHighestSalary(int id) {
        entityValidator.validateExists(employeeRepository.checkIfEmployeeExists(id),
                () -> new EmployeeNotFoundException(id));
        return salaryRepository.getHighestSalaryById(id).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal findEmployeeLowestSalary(int id) {
        entityValidator.validateExists(employeeRepository.checkIfEmployeeExists(id),
                () -> new EmployeeNotFoundException(id));
        return salaryRepository.getLowestSalaryById(id).setScale(2, RoundingMode.HALF_UP);
    }

    public List<BigDecimal> findEmployeeSalaryGrowth(int id) {
        entityValidator.validateExists(employeeRepository.checkIfEmployeeExists(id),
                () -> new EmployeeNotFoundException(id));
        List<BigDecimal> salaries = salaryRepository.getSalaryGrowthById(id).
                stream().
                map((salary) -> salary.setScale(2, RoundingMode.HALF_UP)).
                toList();
        return salaries;
    }

    public Integer findEmployeeChangeSalaryCount(int id) {
        entityValidator.validateExists(employeeRepository.checkIfEmployeeExists(id),
                () -> new EmployeeNotFoundException(id));
        return salaryRepository.getSalaryChangeCountById(id);
    }
}
