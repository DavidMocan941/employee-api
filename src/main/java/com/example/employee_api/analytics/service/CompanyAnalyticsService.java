package com.example.employee_api.analytics.service;

import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.department.dto.DepartmentSalaryBudgetDTO;
import com.example.employee_api.department.mapper.DepartmentMapper;
import com.example.employee_api.department.model.Department;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.TopPaidEmployeeResponseDTO;
import com.example.employee_api.salary.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
public class CompanyAnalyticsService {
    private final DepartmentRepository departmentRepository;
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public CompanyAnalyticsService(DepartmentRepository departmentRepository, SalaryRepository salaryRepository,
                                   EmployeeRepository employeeRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
        this.departmentMapper = departmentMapper;
    }

    public BigDecimal findCompanyAverageSalary() {
        BigDecimal salaryAverage = salaryRepository.getCompanyAverageSalary().
                stream().
                findFirst().
                orElse(null);
        return salaryAverage != null ? salaryAverage.setScale(2, RoundingMode.HALF_UP) : null;
    }

    public BigDecimal findCompanyTotalSalaryBudged() {
        BigDecimal salaryBudget = salaryRepository.getCompanyTotalSalaryBudget().
                stream().
                findFirst().
                orElse(null);
        return salaryBudget;
    }

    public List<TopPaidEmployeeResponseDTO> findCompanyTopPaidEmployees() {
        return employeeRepository.getCompanyTopPaidEmployees();
    }

    public DepartmentSalaryBudgetDTO findDepartmentWithHighestSalaryBudget() {
        return departmentRepository.getDepartmentWithHighestSalaryBudget().stream().findFirst().orElse(null);
    }

    public DepartmentResponseDTO findDepartmentWithMostEmployees() {
        Department department = departmentRepository.getDepartmentWithMostEmployees().
                stream().
                findFirst().
                orElse(null);
        return department != null ? departmentMapper.toDepartmentResponseDTO(department) : null;
    }
}
