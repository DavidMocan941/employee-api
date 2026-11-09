package com.example.employee_api.employee.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.model.Employee;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class EmployeeMapperTest {
  private EmployeeMapper employeeMapper = new EmployeeMapper();

  @Test
  public void toEmployee_everyFieldIsMapped_returnsAMappedEmployee() {
    int positionId = 1;
    int departmentId = 1;
    EmployeeCreateDTO employeeCreateDTO = new EmployeeCreateDTO();
    employeeCreateDTO.setName("David");
    employeeCreateDTO.setSurname("Mocan");
    employeeCreateDTO.setEmail("davidmocan34@gmail.com");
    employeeCreateDTO.setBirth(LocalDate.of(2006, 3, 21));

    Employee employee = employeeMapper.toEmployee(employeeCreateDTO, departmentId, positionId);
    assertAll(
        () -> assertEquals(employeeCreateDTO.getName(), employee.getName()),
        () -> assertEquals(employeeCreateDTO.getSurname(), employee.getSurname()),
        () -> assertEquals(employeeCreateDTO.getEmail(), employee.getEmail()),
        () -> assertEquals(employeeCreateDTO.getBirth(), employee.getBirth()),
        () -> assertEquals(departmentId, employee.getDepartmentId()),
        () -> assertEquals(positionId, employee.getPositionId()));
  }

  @Test
  public void toEmployeeResponseDto_everyFieldIsMapped_returnsAMappedEmployeeResponseDto() {
    Employee employee = new Employee();
    employee.setId(1);
    employee.setName("David");
    employee.setSurname("Mocan");
    employee.setEmail("davidmocan34@gmail.com");
    employee.setHireDate(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(21, 3, 3)));
    employee.setDepartmentId(1);
    employee.setPositionId(1);
    employee.setBirth(LocalDate.of(2006, 3, 21));

    EmployeeResponseDTO dto = employeeMapper.toEmployeeResponseDTO(employee);

    assertAll(
        () -> assertEquals(employee.getId(), dto.getId()),
        () -> assertEquals(employee.getName(), dto.getName()),
        () -> assertEquals(employee.getSurname(), dto.getSurname()),
        () -> assertEquals(employee.getEmail(), dto.getEmail()),
        () -> assertEquals(employee.getHireDate(), dto.getHireDate()),
        () -> assertEquals(employee.getDepartmentId(), dto.getDepartmentId()),
        () -> assertEquals(employee.getPositionId(), dto.getPositionId()),
        () -> assertEquals(employee.getBirth(), dto.getBirth()));
  }
}
