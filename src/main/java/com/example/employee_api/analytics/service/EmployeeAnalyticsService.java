package com.example.employee_api.analytics.service;

import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.salary.SalaryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAnalyticsService {
  private final SalaryRepository salaryRepository;
  private final EmployeeRepository employeeRepository;

  @Autowired
  public EmployeeAnalyticsService(
      SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
    this.salaryRepository = salaryRepository;
    this.employeeRepository = employeeRepository;
  }

  public BigDecimal findEmployeeAvgSalary(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    return salaryRepository.getAverageSalaryById(id).setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal findEmployeeHighestSalary(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    return salaryRepository.getHighestSalaryById(id).setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal findEmployeeLowestSalary(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    return salaryRepository.getLowestSalaryById(id).setScale(2, RoundingMode.HALF_UP);
  }

  public List<BigDecimal> findEmployeeSalaryGrowth(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    List<BigDecimal> salaries =
        salaryRepository.getSalaryGrowthById(id).stream()
            .map((salary) -> salary.setScale(2, RoundingMode.HALF_UP))
            .toList();
    return salaries;
  }

  public Integer findEmployeeChangeSalaryCount(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    return salaryRepository.getSalaryChangeCountById(id);
  }
}
