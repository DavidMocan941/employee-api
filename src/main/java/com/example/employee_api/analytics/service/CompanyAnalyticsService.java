package com.example.employee_api.analytics.service;

import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.department.dto.DepartmentSalaryBudgetDTO;
import com.example.employee_api.department.mapper.DepartmentMapper;
import com.example.employee_api.department.model.Department;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.dto.TopPaidEmployeeResponseDTO;
import com.example.employee_api.salary.SalaryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyAnalyticsService {
  private final DepartmentRepository departmentRepository;
  private final SalaryRepository salaryRepository;
  private final EmployeeRepository employeeRepository;
  private final DepartmentMapper departmentMapper;

  @Autowired
  public CompanyAnalyticsService(
      DepartmentRepository departmentRepository,
      SalaryRepository salaryRepository,
      EmployeeRepository employeeRepository,
      DepartmentMapper departmentMapper) {
    this.departmentRepository = departmentRepository;
    this.salaryRepository = salaryRepository;
    this.employeeRepository = employeeRepository;
    this.departmentMapper = departmentMapper;
  }

  // Do not forget to test methods if query can work without a list
  // Also I have to update methods from other analytics classes
  // After commit and see when and how often commits are used
  public BigDecimal findCompanyAverageSalary() {
    BigDecimal salaryAverage = salaryRepository.getCompanyAverageSalary();
    return salaryAverage != null ? salaryAverage.setScale(2, RoundingMode.HALF_UP) : null;
  }

  public BigDecimal findCompanyTotalSalaryBudged() {
    BigDecimal salaryBudget =
        salaryRepository.getCompanyTotalSalaryBudget().stream().findFirst().orElse(null);
    return salaryBudget;
  }

  public List<TopPaidEmployeeResponseDTO> findCompanyTopPaidEmployees() {
    return employeeRepository.getCompanyTopPaidEmployees();
  }

  public DepartmentSalaryBudgetDTO findDepartmentWithHighestSalaryBudget() {
    return departmentRepository.getDepartmentWithHighestSalaryBudget();
  }

  public DepartmentResponseDTO findDepartmentWithMostEmployees() {
    Department department = departmentRepository.getDepartmentWithMostEmployees();
    return department != null ? departmentMapper.toDepartmentResponseDTO(department) : null;
  }
}
