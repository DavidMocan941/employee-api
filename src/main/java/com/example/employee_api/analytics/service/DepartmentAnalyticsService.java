package com.example.employee_api.analytics.service;

import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.SalaryDistribution;
import com.example.employee_api.salary.SalaryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentAnalyticsService {
  private final SalaryRepository salaryRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;

  @Autowired
  public DepartmentAnalyticsService(
      SalaryRepository salaryRepository,
      DepartmentRepository departmentRepository,
      EmployeeRepository employeeRepository,
      EmployeeMapper employeeMapper) {
    this.salaryRepository = salaryRepository;
    this.departmentRepository = departmentRepository;
    this.employeeRepository = employeeRepository;
    this.employeeMapper = employeeMapper;
  }

  public BigDecimal findAvgSalaryForDepartment(int departmentId) {
    departmentRepository.checkIfDepartmentExists(departmentId);
    return Optional.of(
            salaryRepository
                .getAvgSalaryByDepartmentId(departmentId)
                .setScale(2, RoundingMode.HALF_UP))
        .orElse(BigDecimal.ZERO);
  }

  public BigDecimal findSalaryBudgedForDepartment(int departmentId) {
    departmentRepository.checkIfDepartmentExists(departmentId);
    return Optional.of(
            salaryRepository
                .getTotalSalaryBudgetByDepartmentId(departmentId)
                .setScale(2, RoundingMode.HALF_UP))
        .orElse(BigDecimal.ZERO);
  }

  public EmployeeResponseDTO findHighestPaidEmployeeForDepartment(int departmentId) {
    departmentRepository.checkIfDepartmentExists(departmentId);
    return employeeRepository.getHighestPaidEmployeeInDepartment(departmentId);
  }

  public int findEmployeeCountForDepartment(int departmentId) {
    departmentRepository.checkIfDepartmentExists(departmentId);
    return employeeRepository.getEmployeeCountInDepartment(departmentId);
  }

  public List<SalaryDistribution> findSalaryDistributionForDepartment(int departmentId) {
    departmentRepository.checkIfDepartmentExists(departmentId);
    List<SalaryDistribution> list =
        employeeRepository.getSalaryDistributionByDepartmentId(departmentId);
    return !list.isEmpty() ? list : Collections.emptyList();
  }
}
