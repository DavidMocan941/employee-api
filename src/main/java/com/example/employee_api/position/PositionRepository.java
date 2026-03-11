package com.example.employee_api.position;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PositionRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Integer> findIdByPosition(String position) {
        String sql = "select id from positions where pos_name = ?";
        List<Integer> list = jdbcTemplate.query(sql, (rs, row) -> {
            return rs.getInt("id");
        }, position);
        return list.stream().findFirst();
    }
}
