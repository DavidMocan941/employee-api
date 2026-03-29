package com.example.employee_api.position;

import com.example.employee_api.common.exceptions.PositionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PositionRepository {
  private final JdbcTemplate jdbcTemplate;

  public Integer findIdByPosition(String position) {
    String sql = "select id from positions where pos_name = ?";
    try {
      return jdbcTemplate.queryForObject(sql, Integer.class, position);
    } catch (EmptyResultDataAccessException e) {
      throw new PositionNotFoundException(position);
    }
  }
}
