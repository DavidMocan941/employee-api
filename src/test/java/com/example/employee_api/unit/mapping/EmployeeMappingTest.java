package com.example.employee_api.unit.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EmployeeMappingTest {
  private EmployeeMapper employeeMapper = new EmployeeMapper();

  @Nested
  class ToEmployeeWhenCreate {
    @Test
    void shouldMapEmployeeCreateDtoToEmployee_whenEmployeeCreateDtoIsNotNull() {
      EmployeeCreateDTO createDTO = new EmployeeCreateDTO();
      createDTO.setName("John");
      createDTO.setSurname("Ben");
      createDTO.setEmail("johnBen123@gmail.com");
      createDTO.setBirth(LocalDate.of(2006, 8, 9));
      Employee employee = employeeMapper.toEmployee(createDTO);
      assertEquals("John", employee.getName());
      assertEquals("Ben", employee.getSurname());
      assertEquals("johnBen123@gmail.com", employee.getEmail());
      assertEquals(LocalDate.of(2006, 8, 9), employee.getBirth());
    }

    @Test
    void shouldThrowNullPointerException_whenEmployeeCreateDtoIsNull() {
      assertThrows(NullPointerException.class, () -> employeeMapper.toEmployee(null));
    }
  }
}
