package com.example.employee_api.analytics.service;

import com.example.employee_api.common.EntityValidator;
import com.example.employee_api.common.exceptions.DepartmentNotFoundException;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.department.model.Department;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.employee.model.SalaryDistribution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentAnalyticsService {
    private final SalaryRepository salaryRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EntityValidator entityValidator;
    private final EmployeeMapper employeeMapper;

    public BigDecimal findAvgSalaryForDepartment(int id) {

        validateCommon(departmentRepository.checkIfDepartmentExists(id),id);
        return Optional.
                of(salaryRepository.getAvgSalaryByDepartmentId(id).
                        setScale(2, RoundingMode.HALF_UP)).
                orElse(BigDecimal.ZERO);
    }

    public BigDecimal findSalaryBudgedForDepartment(int id) {
//        entityValidator.validateExists(departmentRepository.checkIfDepartmentExists(id),
//                () -> new DepartmentNotFoundException(id));
        return Optional.of(salaryRepository.getTotalSalaryBudgetByDepartmentId(id).
                        setScale(2, RoundingMode.HALF_UP)).
                orElse(BigDecimal.ZERO);
    }

    public EmployeeResponseDTO findHighestPaidEmployeeForDepartment(int id) {
//        entityValidator.validateExists(departmentRepository.checkIfDepartmentExists(id),
//                () -> new DepartmentNotFoundException(id));
        Employee employee = employeeRepository.getHighestPaidEmployeeInDepartment(id).
                stream().
                findFirst().
                orElse(null);
        return employee != null ? employeeMapper.toEmployeeResponseDTO(employee) : null;
    }

    public Integer findEmployeeCountForDepartment(int id) {
//        entityValidator.validateExists(departmentRepository.checkIfDepartmentExists(id),
//                () -> new DepartmentNotFoundException(id));
        return employeeRepository.getEmployeeCountInDepartment(id).
                stream().
                findFirst()
                .orElse(0);
    }

    public List<SalaryDistribution> findSalaryDistributionForDepartment(int id) {
//        entityValidator.validateExists(departmentRepository.checkIfDepartmentExists(id),
//                () -> new DepartmentNotFoundException(id));
        List<SalaryDistribution> list = employeeRepository.getSalaryDistributionByDepartmentId(id);
        return !list.isEmpty() ? list : Collections.emptyList();
    }

    private void validateCommon(boolean exist,Integer departmentID) {
        if (!exist) {
            throw new DepartmentNotFoundException(departmentID);
        }
    }
}
