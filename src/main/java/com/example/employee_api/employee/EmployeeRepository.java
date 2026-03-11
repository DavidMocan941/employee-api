package com.example.employee_api.employee;

import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.employee.dto.TopPaidEmployeeResponseDTO;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.employee.model.SalaryDistribution;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    public int insertEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into employees(name,surname,email,department_id,position_id,birth," +
                "created_by,created_at) values (?,?,?,?,?,?,?,?);";
        PreparedStatementCreator preparedStatementCreator =
                connect -> getPreparedStatement(employee, connect, sql);
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private static PreparedStatement getPreparedStatement(Employee employee, Connection connect,
                                                          String sql) throws SQLException {
        PreparedStatement ps = connect.prepareStatement(sql, new String[]{"id"});
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getSurname());
        ps.setString(3, employee.getEmail());
        ps.setInt(4, employee.getDepartmentId());
        ps.setInt(5, employee.getPositionId());
        ps.setDate(6, Date.valueOf(employee.getBirth()));
        ps.setInt(7, employee.getCreatedBy());
        ps.setTimestamp(8, Timestamp.valueOf(employee.getCreatedAt()));
        return ps;
    }

    public boolean checkAnExistingEmail(String email) {
        String sql = "select count(*) from employees where email = ? limit 1 ";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    public int countEmployeesOfDepartment(int id) {
        String sql = "select count(*) from employees e join departments d on e.department_id =d.id where d.id=? group by d.id;";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count;
    }


    public void fullyUpdateEmployeeById(Employee employee) {
        String sql = "update employees set name=?,surname=?,email=?,department_id=?,position_id=?,birth=? where id=?;";
        jdbcTemplate.update(sql, employee.getName(), employee.getSurname(),
                employee.getEmail(), employee.getDepartmentId(),
                employee.getPositionId(), employee.getBirth(), employee.getId());

    }

    public Employee findEmployee(int id) {
        String sql = "select id,name,surname,email,hire_date,department_id,position_id,birth from employees where id =?;";
        RowMapper<Employee> rowMapper = (rs, rowNum) ->
                getEmployeeResponseDTO(rs);
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    private static Employee getEmployeeResponseDTO(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setSurname(rs.getString("surname"));
        employee.setEmail(rs.getString("email"));
        employee.setHireDate(rs.getTimestamp("hire_date").toLocalDateTime());
        employee.setDepartmentId(rs.getInt("department_id"));
        employee.setPositionId(rs.getInt("position_id"));
        employee.setBirth(rs.getDate("birth").toLocalDate());
        return employee;
    }

    public boolean checkIfEmployeeExists(int id) {
        String sql = "select count(*) from employees where id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }


    public List<Employee> findAllEmployees() {
        String sql = "select id,name,surname,email,hire_date,department_id,position_id,birth from employees;";
        return jdbcTemplate.query(sql, rs -> {
            List<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setSurname(rs.getString("surname"));
                employee.setEmail(rs.getString("email"));
                employee.setHireDate(rs.getTimestamp("hire_date").toLocalDateTime());
                employee.setDepartmentId(rs.getInt("department_id"));
                employee.setPositionId(rs.getInt("position_id"));
                employee.setBirth(rs.getDate("birth").toLocalDate());
                employees.add(employee);
            }
            return employees;
        });
    }

    public void deleteEmployeeById(int id, Employee employee) {
        String sql = "update employees set is_active=?,deleted_by=?,deleted_at=? where id=?;";
        jdbcTemplate.update(sql, employee.isActive(), employee.getDeletedBy(), employee.getDeletedAt(), id);
    }

    public List<Employee> getHighestPaidEmployeeInDepartment(int departmentId) {
        String sql = "SELECT\n" +
                "\tE.ID AS EMP_ID,\n" +
                "\tNAME,\n" +
                "\tSURNAME,\n" +
                "\tEMAIL,\n" +
                "\tHIRE_DATE,\n" +
                "\tDEPARTMENT_ID,\n" +
                "\tPOSITION_ID,\n" +
                "\tBIRTH\n" +
                "FROM\n" +
                "\tEMPLOYEES E\n" +
                "\tJOIN SALARIES S ON S.EMPLOYEE_ID = E.ID\n" +
                "WHERE\n" +
                "\tDEPARTMENT_ID = ?\n" +
                "\tAND EFFECTIVE_FROM <= NOW()\n" +
                "\tAND (\n" +
                "\t\tEFFECTIVE_TO IS NULL\n" +
                "\t\tOR EFFECTIVE_TO > NOW()\n" +
                "\t)\n" +
                "ORDER BY\n" +
                "\tSALARY DESC\n" +
                "LIMIT\n" +
                "\t1;";

        return jdbcTemplate.query(sql, rs -> {
            List<Employee> list = new ArrayList<>();
            extracted(rs, list);
            return list;
        }, departmentId);
    }

    private static void extracted(ResultSet rs, List<Employee> list) throws SQLException {
        while (rs.next()) {
            Employee employee = new Employee();
            employee.setId(rs.getInt("emp_id"));
            employee.setName(rs.getString("name"));
            employee.setSurname(rs.getString("surname"));
            employee.setEmail(rs.getString("email"));
            employee.setHireDate(rs.getTimestamp("hire_date").toLocalDateTime());
            employee.setDepartmentId(rs.getInt("department_id"));
            employee.setPositionId(rs.getInt("position_id"));
            employee.setBirth(rs.getDate("birth").toLocalDate());
            list.add(employee);
        }
    }

    public List<Integer> getEmployeeCountInDepartment(int id) {
        String sql = "select count(*) as count from employees where department_id=?;";
        return jdbcTemplate.query(sql, rs -> {
            List<Integer> list = new ArrayList<>();
            if (rs.next()) {
                list.add(rs.getInt("count"));
            }
            return list;
        }, id);
    }

    public List<SalaryDistribution> getSalaryDistributionByDepartmentId(int id) {
        String sql = "SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN SALARY <= 2999 THEN '0-3000'\n" +
                "\t\tWHEN SALARY BETWEEN 3000 AND 4999  THEN '3000-4999'\n" +
                "\t\tWHEN SALARY BETWEEN 5000 AND 6999  THEN '5000-6999'\n" +
                "\t\tWHEN SALARY >= 7000 THEN '7000+'\n" +
                "\tEND AS SALARY_RANGE,\n" +
                "\tCOUNT(*) EMPLOYEE_NUMB\n" +
                "FROM\n" +
                "\tSALARIES S\n" +
                "\tJOIN EMPLOYEES E ON S.EMPLOYEE_ID = E.ID\n" +
                "WHERE\n" +
                "\tDEPARTMENT_ID = ?\n" +
                "\tAND EFFECTIVE_FROM < NOW()\n" +
                "\tAND (\n" +
                "\t\tEFFECTIVE_TO IS NULL\n" +
                "\t\tOR EFFECTIVE_TO > NOW()\n" +
                "\t)\n" +
                "GROUP BY\n" +
                "\tSALARY_RANGE\n" +
                "ORDER BY\n" +
                "\tSALARY_RANGE ASC;\n";
        return jdbcTemplate.query(sql, rs -> {
            List<SalaryDistribution> list = new ArrayList<>();
            extracted1(rs, list);
            return list;
        }, id);
    }

    private static void extracted1(ResultSet rs, List<SalaryDistribution> list) throws SQLException {
        while (rs.next()) {
            SalaryDistribution distribution = new SalaryDistribution();
            distribution.setSalaryRange(rs.getString("SALARY_RANGE"));
            distribution.setCountEmployee(rs.getInt("EMPLOYEE_NUMB"));
            list.add(distribution);
        }
    }

    public List<TopPaidEmployeeResponseDTO> getCompanyTopPaidEmployees() {
        String sql = "SELECT\n" +
                "\tE.ID AS EMP_ID,\n" +
                "\tNAME,\n" +
                "\tSURNAME,\n" +
                "\tD.ID AS DEP_ID,\n" +
                "\tDEP_NAME,\n" +
                "\tSALARY\n" +
                "FROM\n" +
                "\tEMPLOYEES E\n" +
                "\tJOIN DEPARTMENTS D ON E.DEPARTMENT_ID = D.ID\n" +
                "\tJOIN SALARIES S ON S.EMPLOYEE_ID = E.ID\n" +
                "WHERE\n" +
                "\tEFFECTIVE_FROM <= NOW()\n" +
                "\tAND (\n" +
                "\t\tEFFECTIVE_TO IS NULL\n" +
                "\t\tOR EFFECTIVE_TO > NOW()\n" +
                "\t)\n" +
                "ORDER BY\n" +
                "\tSALARY DESC\n" +
                "LIMIT\n" +
                "\t5;";
        return jdbcTemplate.query(sql, rs -> {
            List<TopPaidEmployeeResponseDTO> list = new ArrayList<>();
            extracted2(rs, list);
            return list;
        });
    }

    private static void extracted2(ResultSet rs, List<TopPaidEmployeeResponseDTO> list) throws SQLException {
        while (rs.next()) {
            TopPaidEmployeeResponseDTO responseDTO = new TopPaidEmployeeResponseDTO();
            responseDTO.setId(rs.getInt("EMP_ID"));
            responseDTO.setName(rs.getString("NAME"));
            responseDTO.setSurname(rs.getString("SURNAME"));
            responseDTO.setDepartmentResponseDTO(new DepartmentResponseDTO(
                    rs.getInt("DEP_ID"),
                    rs.getString("DEP_NAME")));
            responseDTO.setSalary(rs.getBigDecimal("SALARY"));
            list.add(responseDTO);
        }
    }

    public List<Employee> getEmployeeWithSalaryIncreaseThisYear() {
        String sql = "SELECT\n" +
                "\tID,\n" +
                "\tNAME,\n" +
                "\tSURNAME,\n" +
                "\tEMAIL,\n" +
                "\tHIRE_DATE,\n" +
                "\tDEPARTMENT_ID,\n" +
                "\tPOSITION_ID,\n" +
                "\tBIRTH\n" +
                "FROM\n" +
                "\tEMPLOYEES E\n" +
                "WHERE\n" +
                "\tE.ID IN (\n" +
                "\t\tSELECT\n" +
                "\t\t\tEMPLOYEE_ID\n" +
                "\t\tFROM\n" +
                "\t\t\tSALARIES S1\n" +
                "\t\tWHERE\n" +
                "\t\t\tEFFECTIVE_FROM >= '2026-01-01'\n" +
                "\t\t\tAND SALARY > (\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tSALARY\n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tSALARIES S2\n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\tS2.EMPLOYEE_ID = S1.EMPLOYEE_ID\n" +
                "\t\t\t\t\tAND EFFECTIVE_FROM < '2026-01-01'\n" +
                "\t\t\t\tORDER BY\n" +
                "\t\t\t\t\tEFFECTIVE_FROM DESC\n" +
                "\t\t\t\tLIMIT\n" +
                "\t\t\t\t\t1\n" +
                "\t\t\t)\n" +
                "\t);";
        return jdbcTemplate.query(sql, rs -> {
            return getEmployees(rs);
        });
    }

    private static List<Employee> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> list = new ArrayList<>();
        while (rs.next()) {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setName(rs.getString("name"));
            employee.setSurname(rs.getString("surname"));
            employee.setEmail(rs.getString("email"));
            employee.setHireDate(rs.getTimestamp("hire_date").toLocalDateTime());
            employee.setDepartmentId(rs.getInt("department_id"));
            employee.setPositionId(rs.getInt("position_id"));
            employee.setBirth(rs.getDate("birth").toLocalDate());
            list.add(employee);
        }
        return list;
    }

    public List<Employee> getEmployeeWithSalaryDecrease() {
        String sql = "SELECT\n" +
                "\tID,\n" +
                "\tNAME,\n" +
                "\tSURNAME,\n" +
                "\tEMAIL,\n" +
                "\tHIRE_DATE,\n" +
                "\tDEPARTMENT_ID,\n" +
                "\tPOSITION_ID,\n" +
                "\tBIRTH\n" +
                "FROM\n" +
                "\tEMPLOYEES\n" +
                "WHERE\n" +
                "\tID IN (\n" +
                "\t\tSELECT\n" +
                "\t\t\tEMPLOYEE_ID\n" +
                "\t\tFROM\n" +
                "\t\t\tSALARIES S1\n" +
                "\t\tWHERE\n" +
                "\t\t\tEFFECTIVE_FROM <= NOW()\n" +
                "\t\t\tAND (\n" +
                "\t\t\t\tEFFECTIVE_TO IS NULL\n" +
                "\t\t\t\tOR EFFECTIVE_TO > NOW()\n" +
                "\t\t\t)\n" +
                "\t\t\tAND S1.SALARY < (\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\tS2.SALARY\n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tSALARIES S2\n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\tS2.EMPLOYEE_ID = S1.EMPLOYEE_ID\n" +
                "\t\t\t\t\tAND EFFECTIVE_TO < NOW()\n" +
                "\t\t\t\tORDER BY\n" +
                "\t\t\t\t\tEFFECTIVE_TO DESC\n" +
                "\t\t\t\tLIMIT\n" +
                "\t\t\t\t\t1\n" +
                "\t\t\t)\n" +
                "\t);";
        return jdbcTemplate.query(sql, rs -> {
            List<Employee> employees = new ArrayList<>();
            return getEmployees(rs, employees);
        });
    }

    private static List<Employee> getEmployees(ResultSet rs, List<Employee> employees) throws SQLException {
        while (rs.next()) {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setName(rs.getString("name"));
            employee.setSurname(rs.getString("surname"));
            employee.setEmail(rs.getString("email"));
            employee.setHireDate(rs.getTimestamp("hire_date").toLocalDateTime());
            employee.setDepartmentId(rs.getInt("department_id"));
            employee.setPositionId(rs.getInt("position_id"));
            employee.setBirth(rs.getDate("birth").toLocalDate());
            employees.add(employee);
        }
        return employees;
    }
}
