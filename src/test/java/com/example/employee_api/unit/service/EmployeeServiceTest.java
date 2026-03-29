package com.example.employee_api.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.EmployeeService;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.employee.validation.EmployeeValidator;
import com.example.employee_api.position.PositionRepository;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
  @Mock private DepartmentRepository departmentRepository;
  @Mock private EmployeeRepository employeeRepository;
  @Mock private SalaryRepository salaryRepository;
  @Mock private PositionRepository positionRepository;
  @Mock private EmployeeMapper employeeMapper;
  @Mock private SalaryMapper salaryMapper;
  @Mock private EmployeeValidator employeeValidator;
  @InjectMocks private EmployeeService employeeService;

  @Nested
  class CreateEmployeeAndSalary {

    @Test
    void shouldSetCurrentDate_whenEffectiveFromIsNull() {
      EmployeeCreateDTO dto = new EmployeeCreateDTO();
      dto.setEmail("test@mail.com");
      dto.setDepartmentName("IT");
      dto.setPositionName("Developer");
      dto.setEffectiveFrom(null);

      when(employeeMapper.toEmployee(any())).thenReturn(new Employee());
      when(salaryMapper.toSalary((EmployeeCreateDTO) any())).thenReturn(new Salary());
      doNothing().when(employeeRepository).checkAnExistingEmail(any());
      when(employeeRepository.insertEmployee(any())).thenReturn(1);
      when(salaryRepository.insertSalary(any())).thenReturn(1);
      doNothing().when(employeeValidator).validateWhenCreateEmployee(any());
      when(departmentRepository.findIdByDepartment("IT")).thenReturn(1);
      when(positionRepository.findIdByPosition("Developer")).thenReturn(1);

      employeeService.addEmployeeAndSalary(dto);
      verify(salaryRepository).insertSalary(argThat(salary -> salary.getEffectiveFrom() != null));
    }

    @Test
    void shouldThrowEmailAlreadyExistException_whenEmailExist() {
      EmployeeCreateDTO dto = new EmployeeCreateDTO();
      dto.setEmail("test@mail.com");
      doThrow(new EmailAlreadyExistException(dto.getEmail()))
          .when(employeeRepository)
          .checkAnExistingEmail(dto.getEmail());
      assertThrows(
          EmailAlreadyExistException.class, () -> employeeService.addEmployeeAndSalary(dto));
      verify(employeeRepository, never()).insertEmployee(any());
      verify(salaryRepository, never()).insertSalary(any());
    }
  }
}
