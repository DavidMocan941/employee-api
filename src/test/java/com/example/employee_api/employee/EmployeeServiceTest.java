package com.example.employee_api.employee;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.employee_api.common.exceptions.DepartmentNotFoundException;
import com.example.employee_api.common.exceptions.PositionNotFoundException;
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
import java.util.Optional;

import com.example.employee_api.validation.employee.EmployeeUpdatingValidator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
  @Mock private DepartmentRepository departmentRepository;
  @Mock private PositionRepository positionRepository;
  @Mock private EmployeeMapper employeeMapper;
  @Mock private EmployeeRepository employeeRepository;
  @Mock private SalaryMapper salaryMapper;
  @Mock private SalaryRepository salaryRepository;
  @Mock private EmployeeCreationValidator validator;
  @Mock private EmployeeUpdatingValidator employeeUpdatingValidator;
  @InjectMocks private EmployeeService employeeService;

  @Nested
  class AddEmployeeAndSalaryMethod {
    @Test
    void addEmployeeAndSalary_everythingValid_insertEmployeeAndSalary() {
      String department = "IT", position = "Backend Developer";
      int departmentId = 1, positionId = 2;

      EmployeeCreateDto dto = new EmployeeCreateDto();
      dto.setDepartmentName(department);
      dto.setPositionName(position);

      Employee employee = new Employee();
      employee.setDepartmentId(departmentId);
      employee.setPositionId(positionId);
      int employeeKey = 1;
      Salary salary = new Salary();
      int salaryKey = 1;
      EmployeeResponseDto employeeResponseDTO = new EmployeeResponseDto();
      when(departmentRepository.findIdByDepartment(department))
          .thenReturn(Optional.of(departmentId));
      when(positionRepository.findIdByPosition(position)).thenReturn(Optional.of(positionId));
      when(employeeMapper.toEmployee(dto, departmentId, positionId)).thenReturn(employee);
      when(employeeRepository.insertEmployee(employee)).thenReturn(employeeKey);
      when(salaryMapper.toSalary(dto, employeeKey)).thenReturn(salary);
      when(salaryRepository.insertSalary(salary)).thenReturn(salaryKey);
      when(employeeMapper.toEmployeeResponseDto(employee)).thenReturn(employeeResponseDTO);

      EmployeeResponseDto responseDTO = employeeService.addEmployeeAndSalary(dto);
      assertSame(employeeResponseDTO, responseDTO);

      InOrder inOrder =
          Mockito.inOrder(
              validator,
              departmentRepository,
              positionRepository,
              employeeMapper,
              employeeRepository,
              salaryMapper,
              salaryRepository,
              employeeMapper);
      inOrder.verify(validator).validate(dto);
      inOrder.verify(departmentRepository).findIdByDepartment(dto.getDepartmentName());
      inOrder.verify(positionRepository).findIdByPosition(dto.getPositionName());
      inOrder.verify(employeeMapper).toEmployee(dto, departmentId, positionId);
      inOrder.verify(employeeRepository).insertEmployee(employee);
      inOrder.verify(salaryMapper).toSalary(dto, employeeKey);
      inOrder.verify(salaryRepository).insertSalary(salary);
      inOrder.verify(employeeMapper).toEmployeeResponseDto(employee);
    }

    @Test
    void addEmployeeAndSalary_departmentIsNotFound_nothingExecutesAfterException() {
      String department = "IT";
      EmployeeCreateDto dto = new EmployeeCreateDto();
      dto.setDepartmentName(department);
      when(departmentRepository.findIdByDepartment(department)).thenReturn(Optional.empty());

      assertThrows(
          DepartmentNotFoundException.class, () -> employeeService.addEmployeeAndSalary(dto));

      verify(departmentRepository).findIdByDepartment(department);
      verifyNoMoreInteractions(
          positionRepository, employeeMapper, employeeRepository, salaryMapper, salaryRepository);
    }

    @Test
    void addEmployeeAndSalary_positionIsNotFound_nothingExecutesAfterException() {
      String department = "IT";
      String position = "Backend Developer";
      EmployeeCreateDto dto = new EmployeeCreateDto();
      dto.setDepartmentName(department);
      dto.setPositionName(position);
      when(departmentRepository.findIdByDepartment(department)).thenReturn(Optional.of(1));
      when(positionRepository.findIdByPosition(position)).thenReturn(Optional.empty());

      assertThrows(
          PositionNotFoundException.class, () -> employeeService.addEmployeeAndSalary(dto));

      verify(departmentRepository).findIdByDepartment(department);
      verify(positionRepository).findIdByPosition(position);
      verifyNoMoreInteractions(employeeMapper, employeeRepository, salaryMapper, salaryRepository);
    }
  }

  @Nested
  class UpdateEmployee {
    @Test
    void updateEmployee_everythingValid_updateEmployee() {
      EmployeePutDto dto = new EmployeePutDto();
      dto.setDepartmentName("IT");
      dto.setPositionName("Software Developer");
      Employee employee = new Employee();
      EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();

      when(departmentRepository.findIdByDepartment("IT")).thenReturn(Optional.of(1));
      when(positionRepository.findIdByPosition("Software Developer")).thenReturn(Optional.of(1));
      when(employeeMapper.toEmployee(1, dto, 1, 1)).thenReturn(employee);
      when(employeeMapper.toEmployeeResponseDto(employee)).thenReturn(employeeResponseDto);
      EmployeeResponseDto responseDto = employeeService.updateEmployee(1, dto);

      assertEquals(employeeResponseDto, responseDto);

      InOrder inOrder =
          inOrder(
              employeeUpdatingValidator,
              departmentRepository,
              positionRepository,
              employeeMapper,
              employeeRepository);

      inOrder.verify(employeeUpdatingValidator).validate(any());
      inOrder.verify(departmentRepository).findIdByDepartment("IT");
      inOrder.verify(positionRepository).findIdByPosition("Software Developer");
      inOrder.verify(employeeMapper).toEmployee(1, dto, 1, 1);
      inOrder.verify(employeeRepository).updateEmployee(employee);
      inOrder.verify(employeeMapper).toEmployeeResponseDto(employee);
    }

    @Test
    void updateEmployee_throwsDepartmentNotFoundException_nothingElseIsExecuted() {
      EmployeePutDto dto = new EmployeePutDto();
      dto.setDepartmentName("IT");
      dto.setPositionName("Software Developer");

      when(departmentRepository.findIdByDepartment("IT")).thenReturn(Optional.empty());

      assertThrows(DepartmentNotFoundException.class, () -> employeeService.updateEmployee(1, dto));
      InOrder inOrder = inOrder(employeeUpdatingValidator, departmentRepository);
      inOrder.verify(employeeUpdatingValidator).validate(any());
      inOrder.verify(departmentRepository).findIdByDepartment("IT");
      verify(positionRepository, never()).findIdByPosition(any());
      verify(employeeMapper, never()).toEmployee(anyByte(), any(), anyByte(), anyByte());
      verify(employeeRepository, never()).updateEmployee(any());
      verify(employeeMapper, never()).toEmployeeResponseDto(any());
    }

    @Test
    void updateEmployee_throwsPositionNotFoundException_nothingElseIsExecuted() {
      EmployeePutDto dto = new EmployeePutDto();
      dto.setDepartmentName("IT");
      dto.setPositionName("Software Developer");

      when(departmentRepository.findIdByDepartment("IT")).thenReturn(Optional.of(1));
      when(positionRepository.findIdByPosition("Software Developer")).thenReturn(Optional.empty());

      assertThrows(PositionNotFoundException.class, () -> employeeService.updateEmployee(1, dto));
      InOrder inOrder =
          inOrder(employeeUpdatingValidator, departmentRepository, positionRepository);
      inOrder.verify(employeeUpdatingValidator).validate(any());
      inOrder.verify(departmentRepository).findIdByDepartment(anyString());
      inOrder.verify(positionRepository).findIdByPosition("Software Developer");
      verify(employeeMapper, never()).toEmployee(anyByte(), any(), anyByte(), anyByte());
      verify(employeeRepository, never()).updateEmployee(any());
      verify(employeeMapper, never()).toEmployeeResponseDto(any());
    }
  }
}
