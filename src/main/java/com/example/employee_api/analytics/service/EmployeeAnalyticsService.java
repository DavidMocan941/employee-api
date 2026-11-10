package com.example.employee_api.analytics.service;

import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.salary.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAnalyticsService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

   /* public BigDecimal findEmployeeAvgSalary(int id) {
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
    }*/
}
