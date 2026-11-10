package com.example.employee_api.employee;

import com.example.employee_api.common.exceptions.*;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.dto.EmployeeCreateDto;
import com.example.employee_api.employee.dto.EmployeePutDto;
import com.example.employee_api.employee.dto.EmployeeResponseDto;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.position.PositionRepository;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import com.example.employee_api.validation.context.EmployeeUpdateContext;
import com.example.employee_api.validation.employee.EmployeeCreationValidator;
import com.example.employee_api.validation.employee.EmployeeUpdatingValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final SalaryRepository salaryRepository;
  private final PositionRepository positionRepository;
  private final EmployeeMapper employeeMapper;
  private final SalaryMapper salaryMapper;
  private final EmployeeCreationValidator employeeCreationValidator;
  private final EmployeeUpdatingValidator employeeUpdatingValidator;

  @Transactional
  public EmployeeResponseDto addEmployeeAndSalary(EmployeeCreateDto employeeCreateDTO) {

    employeeCreationValidator.validate(employeeCreateDTO);

    int departmentId = departmentResolver(employeeCreateDTO.getDepartmentName());
    int positionId = positionResolver(employeeCreateDTO.getPositionName());

    Employee employee = employeeMapper.toEmployee(employeeCreateDTO, departmentId, positionId);
    int employeeKey = employeeRepository.insertEmployee(employee);

    Salary salary = salaryMapper.toSalary(employeeCreateDTO, employeeKey);
    salaryRepository.insertSalary(salary);

    return employeeMapper.toEmployeeResponseDto(employee);
  }

  public EmployeeResponseDto updateEmployee(int id, EmployeePutDto dto) {

    employeeUpdatingValidator.validate(new EmployeeUpdateContext(id, dto));

    int departmentId = departmentResolver(dto.getDepartmentName());
    int positionId = positionResolver(dto.getPositionName());

    Employee employee = employeeMapper.toEmployee(id, dto, departmentId, positionId);

    employeeRepository.updateEmployee(employee);

    return employeeMapper.toEmployeeResponseDto(employee);
  }

  public EmployeeResponseDto getEmployee(int id) {
    boolean exists = employeeRepository.checkIfEmployeeExists(id);
    if (!exists) {
      throw new EmployeeNotFoundException(id);
    }
    Employee employee = employeeRepository.findEmployee(id);
    EmployeeResponseDto responseDTO = employeeMapper.toEmployeeResponseDto(employee);
    return responseDTO;
  }

  public List<EmployeeResponseDto> getAllEmployees() {
    List<EmployeeResponseDto> response =
        employeeRepository.findAllEmployees().stream()
            .sorted(Comparator.comparing(Employee::getId).reversed())
            .map(employeeMapper::toEmployeeResponseDto)
            .toList();
    return response;
  }

  public void deleteEmployee(int id) {
    boolean employeeExists = employeeRepository.checkIfEmployeeExists(id);
    if (employeeExists) {
      boolean activeSalaryExists = salaryRepository.checkIfEmployeeHasActiveSalary(id);
      if (activeSalaryExists) {
        throw new ActiveSalaryExistsException();
      }
    } else throw new EmployeeNotFoundException(id);
    Employee employee = new Employee();
    //auditService.setAuditFieldsWhenDelete(employee);
    employee.setActive(false);
    employeeRepository.deleteEmployeeById(id, employee);
  }

  private int departmentResolver(String department) {
    return departmentRepository
        .findIdByDepartment(department)
        .orElseThrow(() -> new DepartmentNotFoundException(department));
  }

  private int positionResolver(String position) {
    return positionRepository
        .findIdByPosition(position)
        .orElseThrow(() -> new PositionNotFoundException(position));
  }
}
