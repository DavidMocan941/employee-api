package com.example.employee_api.analytics.service;

import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.salary.dto.SalaryResponseDTO;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryAnalyticsService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final SalaryRepository salaryRepository;
    private final SalaryMapper salaryMapper;

    public List<EmployeeResponseDTO> findEmployeesWithSalaryIncreaseThisYear() {
        List<Employee> employees = employeeRepository.getEmployeeWithSalaryIncreaseThisYear();
        return !employees.isEmpty() ? employees.
                stream().
                map(employee -> employeeMapper.toEmployeeResponseDTO(employee)).
                toList() : Collections.emptyList();
    }

    public List<EmployeeResponseDTO> findEmployeesWithSalaryDecrease() {
        List<Employee> employees = employeeRepository.getEmployeeWithSalaryDecrease();
        return !employees.isEmpty() ? employees.
                stream().
                map(employee -> employeeMapper.toEmployeeResponseDTO(employee)).
                toList() : Collections.emptyList();
    }

    public List<SalaryResponseDTO> findSalaryChangesInLastMonths(int months) {
        List<Salary> salaries = salaryRepository.getSalaryChangesInLastMonths(months);
        return !salaries.isEmpty() ? salaries.
                stream().
                map(salary -> salaryMapper.toSalaryResponseDTO(salary)).
                toList() : Collections.emptyList();
    }

    public List<BigDecimal> findDepartmentSalaryGrowth(int id) {
        List<BigDecimal> differences = salaryRepository.getDepartmentSalaryGrowth(id);
        return !differences.isEmpty() ? differences : Collections.emptyList();
    }
}
