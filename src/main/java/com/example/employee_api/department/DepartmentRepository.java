package com.example.employee_api.department;

import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.department.dto.DepartmentSalaryBudgetDTO;
import com.example.employee_api.department.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Integer> findIdByDepartment(String department) {
        String sql = "select id from departments where dep_name = ?";
        List<Integer> list = jdbcTemplate.query(sql, (rs, row) -> {
            return rs.getInt("id");
        }, department);
        return list.stream().findFirst();
    }

    public int deleteDepartmentFromDb(int id) {
        String sql = "delete from departments where id = ?;";
        return jdbcTemplate.update(connect -> {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, id);
            return ps;
        });
    }

    public boolean checkIfDepartmentExists(int id) {
        String sql = "select from departments where id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;

    }

    public List<DepartmentSalaryBudgetDTO> getDepartmentWithHighestSalaryBudget() {
        String sql = "SELECT\n" +
                "\tD.ID AS DEP_ID,\n" +
                "\tD.DEP_NAME AS DEPARTMENT_NAME,\n" +
                "\tSUM(S.SALARY) AS BUDGET\n" +
                "FROM\n" +
                "\tEMPLOYEES E\n" +
                "\tJOIN SALARIES S ON E.ID = S.EMPLOYEE_ID\n" +
                "\tJOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.ID\n" +
                "WHERE\n" +
                "\tS.EFFECTIVE_FROM <= NOW()\n" +
                "\tAND (\n" +
                "\t\tS.EFFECTIVE_TO IS NULL\n" +
                "\t\tOR S.EFFECTIVE_TO > NOW()\n" +
                "\t)\n" +
                "GROUP BY\n" +
                "\tD.ID,\n" +
                "\tD.DEP_NAME\n" +
                "ORDER BY\n" +
                "\tBUDGET DESC\n" +
                "LIMIT\n" +
                "\t1;";
        return jdbcTemplate.query(sql, rs -> {
            List<DepartmentSalaryBudgetDTO> list = new ArrayList<>();
            if (rs.next()) {
                DepartmentSalaryBudgetDTO budgetDTO = new DepartmentSalaryBudgetDTO();
                budgetDTO.setDepartmentResponseDTO(new DepartmentResponseDTO(
                        rs.getInt("DEP_ID"),
                        rs.getString("DEPARTMENT_NAME")));
                budgetDTO.setSalaryBudget(rs.getBigDecimal("BUDGET"));
                list.add(budgetDTO);
            }
            return list;
        });
    }

    public List<Department> getDepartmentWithMostEmployees() {
        String sql = "select d.id as dep_id,dep_name, count(*) as emp_count from employees e join departments d on e.department_id = d.id group by d.id,dep_name order by emp_count desc limit 1";
        return jdbcTemplate.query(sql, rs -> {
            List<Department> list = new ArrayList<>();
            if (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("dep_id"));
                department.setDepartmentName(rs.getString("dep_name"));
                list.add(department);
            }
            return list;
        });
    }
}
