package com.example.employee_api.unit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.employee_api.common.exceptions.PositionNotFoundException;
import com.example.employee_api.position.PositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class PositionRepositoryTest {
  @Mock JdbcTemplate jdbcTemplate;
  @InjectMocks PositionRepository positionRepository;

  @Test
  void shouldThrowPositionNotFoundException_whenPositionWasNotFound() {
    String position = "Unknown";
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(position)))
        .thenThrow(new EmptyResultDataAccessException(1));
    assertThrows(
        PositionNotFoundException.class, () -> positionRepository.findIdByPosition(position));
  }

  @Test
  void shouldReturnIdOfPosition_whenPositionWasFound() {
    String position = "Software Engineer";
    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(position))).thenReturn(1);
    assertEquals(1, positionRepository.findIdByPosition(position));
  }
}
