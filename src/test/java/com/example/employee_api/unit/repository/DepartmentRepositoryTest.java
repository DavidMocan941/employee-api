package com.example.employee_api.unit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.employee_api.common.exceptions.DepartmentNotFoundException;
import com.example.employee_api.department.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class DepartmentRepositoryTest {
  @InjectMocks DepartmentRepository departmentRepository;
  @Mock JdbcTemplate jdbcTemplate;

  @Test
  void shouldThrowDepartmentNotFoundException_whenDepartmentWasNotFound() {
    String department = "Unknown";
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(department)))
        .thenThrow(new EmptyResultDataAccessException(1));
    assertThrows(
        DepartmentNotFoundException.class,
        () -> departmentRepository.findIdByDepartment(department));
  }

  @Test
  void shouldReturnIdOfDepartment_whenDepartmentWasFound() {
    String department = "IT";
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(department))).thenReturn(1);
    assertEquals(1, departmentRepository.findIdByDepartment(department));
  }
}
