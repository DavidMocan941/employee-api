package com.example.employee_api.employee;

import com.example.employee_api.common.exceptions.*;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.dto.EmployeeAndSalaryResponseDTO;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.dto.EmployeePutDTO;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.employee.validation.EmployeeValidator;
import com.example.employee_api.position.PositionRepository;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class EmployeeService {
  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;
  private final PositionRepository positionRepository;
  private final EmployeeMapper employeeMapper;
  private final SalaryMapper salaryMapper;
  private final EmployeeValidator employeeValidator;

  public EmployeeService(
      DepartmentRepository departmentRepository,
      EmployeeRepository employeeRepository,
      SalaryRepository salaryRepository,
      PositionRepository positionRepository,
      EmployeeMapper employeeMapper,
      SalaryMapper salaryMapper,
      EmployeeValidator employeeValidator) {
    this.departmentRepository = departmentRepository;
    this.employeeRepository = employeeRepository;
    this.salaryRepository = salaryRepository;
    this.positionRepository = positionRepository;
    this.employeeMapper = employeeMapper;
    this.salaryMapper = salaryMapper;
    this.employeeValidator = employeeValidator;
  }

  @Transactional
  public EmployeeAndSalaryResponseDTO addEmployeeAndSalary(EmployeeCreateDTO employeeCreateDTO) {
    log.info("Creating an employee with email: {}", employeeCreateDTO.getEmail());
    employeeRepository.checkAnExistingEmail(employeeCreateDTO.getEmail());
    LocalDate effectiveFrom =
        Optional.ofNullable(employeeCreateDTO.getEffectiveFrom()).orElse(LocalDate.now());
    employeeCreateDTO.setEffectiveFrom(effectiveFrom);
    employeeValidator.validateWhenCreateEmployee(employeeCreateDTO);
    log.debug(
        "Resolving department '{}' and position '{}'",
        employeeCreateDTO.getDepartmentName(),
        employeeCreateDTO.getPositionName());
    Employee employee = employeeMapper.toEmployee(employeeCreateDTO);
    employee.setDepartmentId(
        departmentRepository.findIdByDepartment(employeeCreateDTO.getDepartmentName()));
    employee.setPositionId(
        positionRepository.findIdByPosition(employeeCreateDTO.getPositionName()));
    employee.setCreatedBy(1);
    int employeeKey = employeeRepository.insertEmployee(employee);
    log.info("Successfully created an employee with ID {}", employeeKey);
    Salary salary = salaryMapper.toSalary(employeeCreateDTO);
    salary.setEffectiveFrom(effectiveFrom);
    salary.setEmployeeId(employeeKey);
    salary.setCreatedBy(1);
    log.info("Trying to add the salary for the new employee");
    int salaryKey = salaryRepository.insertSalary(salary);
    log.info("Successfully created a salary with ID {}", salaryKey);
    return employeeRepository.getEmployeeAndSalaryResponseDTO(employeeKey);
  }

  public EmployeeResponseDTO getUpdatedEmployee(int id, EmployeePutDTO employeePutDTO) {
    log.info("Checking if employee exists with id: {}", id);
    employeeRepository.checkIfEmployeeExists(id);
    log.info("Checking an existing email: {}", employeePutDTO.getEmail());
    employeeRepository.checkAnExistingEmail(employeePutDTO.getEmail());
    log.debug(
        "Resolving department: {} and position: {}",
        employeePutDTO.getDepartmentName(),
        employeePutDTO.getPositionName());
    int departmentId = departmentRepository.findIdByDepartment(employeePutDTO.getDepartmentName());
    int positionId = positionRepository.findIdByPosition(employeePutDTO.getPositionName());
    log.debug("Mapping EmployeePutDTO to Employee entity");
    Employee employee = employeeMapper.toEmployee(id, employeePutDTO, departmentId, positionId);
    log.info("Update employee with id: {}", id);
    employeeRepository.fullyUpdateEmployeeById(employee);
    return employeeRepository.findEmployee(id);
  }

  public EmployeeResponseDTO getEmployee(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    return employeeRepository.findEmployee(id);
  }

  public List<EmployeeResponseDTO> getAllEmployees() {
    return employeeRepository.findAllEmployees();
  }

  public void deleteEmployee(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    salaryRepository.checkIfEmployeeHasActiveSalary(id);
    Employee employee = new Employee();
    employee.setActive(false);
    employeeRepository.deleteEmployeeById(id, employee);
  }
}
