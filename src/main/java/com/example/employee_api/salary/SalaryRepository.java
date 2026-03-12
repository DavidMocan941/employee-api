package com.example.employee_api.salary;

import com.example.employee_api.common.exceptions.NoActiveSalaryException;
import com.example.employee_api.salary.model.Salary;
import com.example.employee_api.salary.model.SalaryEndDate;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SalaryRepository {
  private final JdbcTemplate jdbcTemplate;

  public int insertSalary(Salary salary) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql =
        "insert into salaries(salary,currency,effective_from,effective_to,employee_id,created_by,created_at) values"
            + " (?,?,?,?,?,?,?)";
    PreparedStatementCreator preparedStatementCreator =
        connection -> getPreparedStatement(salary, connection, sql);
    jdbcTemplate.update(preparedStatementCreator, keyHolder);
    return keyHolder.getKey().intValue();
  }

  private static PreparedStatement getPreparedStatement(
      Salary salary, Connection connect, String sql) throws SQLException {
    PreparedStatement ps = connect.prepareStatement(sql, new String[] {"id"});
    ps.setBigDecimal(1, salary.getSalary());
    ps.setString(2, salary.getCurrency());
    ps.setDate(3, Date.valueOf(salary.getEffectiveFrom()));
    if (salary.getEffectiveTo() != null) {
      ps.setDate(4, Date.valueOf(salary.getEffectiveTo()));
    } else ps.setNull(4, Types.DATE);
    ps.setInt(5, salary.getEmployeeId());
    ps.setInt(6, salary.getCreatedBy());
    ps.setTimestamp(7, Timestamp.valueOf(salary.getCreatedAt()));
    return ps;
  }

  public int insertNewSalary(Salary salary) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    String sql =
        "insert into salaries (salary,currency,effective_from,effective_to,employee_id,created_by,created_at)"
            + " values(?,?,?,?,?,?,?);";
    jdbcTemplate.update(
        connect -> {
          return getPreparedStatementForNewSalary(salary, connect, sql);
        },
        keyHolder);
    return keyHolder.getKey().intValue();
  }

  private static PreparedStatement getPreparedStatementForNewSalary(
      Salary salary, Connection connect, String sql) throws SQLException {
    PreparedStatement ps = connect.prepareStatement(sql, new String[] {"id"});
    ps.setBigDecimal(1, salary.getSalary());
    ps.setString(2, salary.getCurrency());
    ps.setDate(3, Date.valueOf(salary.getEffectiveFrom()));
    if (salary.getEffectiveTo() != null) {
      ps.setDate(4, Date.valueOf(salary.getEffectiveTo()));
    } else ps.setNull(4, Types.DATE);
    ps.setInt(5, salary.getEmployeeId());
    ps.setInt(6, salary.getCreatedBy());
    ps.setTimestamp(7, Timestamp.valueOf(salary.getCreatedAt()));
    return ps;
  }

  public SalaryEndDate findCurrentSalaryEndDate(int employeeId, SalaryEndDate salaryEndDate) {
    String sql =
        "select id,effective_to from salaries where employee_id = ?"
            + " and effective_from <=now() and(effective_to>now() or effective_to is null);";

    return jdbcTemplate.query(
        sql,
        new Object[] {employeeId},
        rs -> {
          if (rs.next()) {
            LocalDate effectiveTo = rs.getDate("effective_to").toLocalDate();
            salaryEndDate.setId(rs.getInt("id"));
            if (effectiveTo != null) {
              salaryEndDate.setEffectiveTo(effectiveTo);
            }
            salaryEndDate.setExists(true);
          } else {
            salaryEndDate.setExists(false);
          }
          return salaryEndDate;
        });
  }

  public LocalDate findCurrentSalaryStartDate(int employeeId) {
    String sql =
        "select effective_from from salaries where employee_id=? and effective_from<=now()"
            + " and (effective_to is null or effective_to>now())";
    LocalDate effectiveFrom = jdbcTemplate.queryForObject(sql, LocalDate.class, employeeId);
    return effectiveFrom;
  }

  public void insertPastSalaryEndDate(int id, LocalDate effectiveTo) {
    String sql = "update salaries set effective_to = ? where id = ?;";
    jdbcTemplate.update(
        sql,
        ps -> {
          ps.setDate(1, Date.valueOf(effectiveTo));
          ps.setInt(2, id);
        });
  }

  public List<Integer> getEmployeesId() {
    String sql = "select employee_id from salaries;";
    return jdbcTemplate.query(
        sql,
        rs -> {
          List<Integer> list = new ArrayList<>();
          while (rs.next()) {
            list.add(rs.getInt("employee_id"));
          }
          return list;
        });
  }

  public Integer updateEffectiveTo(int employeeId, Salary salary) {
    String sql =
        "update salaries set effective_to=?,updated_at =?,updated_by=? where employee_id=?"
            + " and effective_from <=now() and(effective_to is null or effective_to>now()) returning id;";
    return jdbcTemplate.queryForObject(
        sql,
        Integer.class,
        salary.getEffectiveTo(),
        Timestamp.valueOf(salary.getUpdatedAt()),
        salary.getUpdatedBy(),
        employeeId);
  }

  public Integer updateEmployeeSalary(int employeeId, Salary salary) {
    String sql =
        "update salaries set salary=?,updated_at=?,updated_by=? where employee_id=?"
            + " and effective_from <= now() and(effective_to is null or effective_to>now()) returning id;";
    return jdbcTemplate.queryForObject(
        sql,
        Integer.class,
        salary.getSalary(),
        Timestamp.valueOf(salary.getUpdatedAt()),
        salary.getUpdatedBy(),
        employeeId);
  }

  public Salary getSalaryById(int id) {
    String sql =
        "select id,salary,currency,effective_from,effective_to,employee_id from salaries where id =?;";
    return jdbcTemplate.queryForObject(
        sql,
        (rs, rowNum) -> {
          return getSalaryResponseDTO(rs);
        },
        id);
  }

  private static Salary getSalaryResponseDTO(ResultSet rs) throws SQLException {
    Salary salary = new Salary();
    salary.setId(rs.getInt("id"));
    salary.setSalary(rs.getBigDecimal("salary"));
    salary.setCurrency(rs.getString("currency"));
    salary.setEffectiveFrom(rs.getDate("effective_from").toLocalDate());
    if (rs.getDate("effective_to") != null) {
      salary.setEffectiveTo(rs.getDate("effective_to").toLocalDate());
    } else salary.setEffectiveTo(null);
    salary.setEmployeeId(rs.getInt("employee_id"));
    return salary;
  }

  public void checkIfEmployeeHasActiveSalary(int employeeId) {
    String sql =
        "select count(*) from salaries where employee_id=? and effective_from<now() and (effective_to is null or effective_to > now())";
    Integer result = jdbcTemplate.queryForObject(sql, Integer.class, employeeId);
    if (result == 0) throw new NoActiveSalaryException();
  }

  public List<Salary> findEmployeeSalaryHistory(int employeeId) {
    String sql =
        "select id,salary,currency,effective_from,effective_to,employee_id from salaries where employee_id=?;";
    return jdbcTemplate.query(
        sql,
        rs -> {
          List<Salary> list = new ArrayList<>();
          while (rs.next()) {
            extracted(rs, list);
          }
          return list;
        },
        employeeId);
  }

  private static void extracted(ResultSet rs, List<Salary> list) throws SQLException {
    Salary salary = new Salary();
    salary.setId(rs.getInt("id"));
    salary.setSalary(rs.getBigDecimal("salary"));
    salary.setCurrency(rs.getString("currency"));
    salary.setEffectiveFrom(rs.getDate("effective_from").toLocalDate());
    Date effectiveTo = rs.getDate("effective_to");
    if (effectiveTo != null) {
      salary.setEffectiveTo(effectiveTo.toLocalDate());
    } else salary.setEffectiveTo(null);
    salary.setEmployeeId(rs.getInt("employee_id"));
    list.add(salary);
  }

  // Salary analytics
  public BigDecimal getAverageSalaryById(int id) {
    String sql = "select avg(salary) from salaries where employee_id =? group by employee_id;";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
  }

  public BigDecimal getHighestSalaryById(int id) {
    String sql = "select max(salary) from salaries where employee_id = ?;";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
  }

  public BigDecimal getLowestSalaryById(int id) {
    String sql = "select min(salary) from salaries where employee_id = ?;";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
  }

  public List<BigDecimal> getSalaryGrowthById(int id) {
    String sql = "select salary from salaries where employee_id=? order by salary asc;";
    return jdbcTemplate.query(
        sql,
        rs -> {
          List<BigDecimal> list = new ArrayList<>();
          while (rs.next()) {
            list.add(rs.getBigDecimal("salary"));
          }
          return list;
        },
        id);
  }

  public Integer getSalaryChangeCountById(int id) {
    String sql = "select count(*) from salaries where employee_id=?;";
    return jdbcTemplate.queryForObject(sql, Integer.class, id);
  }

  public BigDecimal getAvgSalaryByDepartmentId(int id) {
    String sql =
        "select avg(salary) from salaries s"
            + " join employees e on s.employee_id=e.id"
            + " where department_id=? and effective_from<= now()"
            + " and (effective_to is null or effective_to > now());";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
  }

  public BigDecimal getTotalSalaryBudgetByDepartmentId(int id) {
    String sql =
        "select sum(salary) from salaries s "
            + "join employees e on s.employee_id=e.id"
            + " where department_id=? and effective_from<=now()"
            + " and (effective_to is null or effective_to>now());";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
  }

  public List<BigDecimal> getCompanyTotalSalaryBudget() {
    String sql =
        "select sum(salary) as salary_sum from salaries where"
            + " effective_from <=now() and"
            + "(effective_to is null or effective_to>now());";
    return jdbcTemplate.query(
        sql,
        rs -> {
          List<BigDecimal> list = new ArrayList<>();
          if (rs.next()) {
            list.add(rs.getBigDecimal("salary_sum"));
          }
          return list;
        });
  }

  public BigDecimal getCompanyAverageSalary() {
    String sql =
        "select avg(salary) as salary_avg from salaries where"
            + " effective_from <=now() and"
            + "(effective_to is null or effective_to>now());";
    return jdbcTemplate.queryForObject(sql, BigDecimal.class);
  }

  public List<Salary> getSalaryChangesInLastMonths(int months) {
    LocalDate cutOffMonths = LocalDate.now().minusMonths(months);
    String sql =
        "select id,salary,currency,effective_from,effective_to,employee_id from salaries where effective_from>=?";
    return jdbcTemplate.query(
        sql,
        rs -> {
          return getSalaries(rs);
        },
        cutOffMonths);
  }

  private static List<Salary> getSalaries(ResultSet rs) throws SQLException {
    List<Salary> salaries = new ArrayList<>();
    while (rs.next()) {
      Salary salary = new Salary();
      salary.setId(rs.getInt("id"));
      salary.setSalary(rs.getBigDecimal("salary"));
      salary.setCurrency(rs.getString("currency"));
      salary.setEffectiveFrom(rs.getDate("effective_from").toLocalDate());
      Date effectiveTo = rs.getDate("effective_to");
      if (effectiveTo != null) {
        salary.setEffectiveTo(effectiveTo.toLocalDate());
      } else salary.setEffectiveTo(null);
      salary.setEmployeeId(rs.getInt("employee_id"));
      salaries.add(salary);
    }
    return salaries;
  }

  public List<BigDecimal> getDepartmentSalaryGrowth(int id) {
    String sql =
        "SELECT\n"
            + "\tACTIVE.SALARY - PREVIOUS.SALARY AS SALARY_DIFFERENCE\n"
            + "FROM\n"
            + "\tSALARIES ACTIVE\n"
            + "\tJOIN SALARIES PREVIOUS ON ACTIVE.EMPLOYEE_ID = PREVIOUS.EMPLOYEE_ID\n"
            + "\tJOIN EMPLOYEES E ON E.ID = ACTIVE.EMPLOYEE_ID\n"
            + "WHERE\n"
            + "\tDEPARTMENT_ID = ?\n"
            + "\tAND ACTIVE.EFFECTIVE_FROM <= NOW()\n"
            + "\tAND (\n"
            + "\t\tACTIVE.EFFECTIVE_TO IS NULL\n"
            + "\t\tOR ACTIVE.EFFECTIVE_TO > NOW()\n"
            + "\t)\n"
            + "\tAND PREVIOUS.EFFECTIVE_TO = (\n"
            + "\t\tSELECT\n"
            + "\t\t\tMAX(EFFECTIVE_TO)\n"
            + "\t\tFROM\n"
            + "\t\t\tSALARIES\n"
            + "\t\tWHERE\n"
            + "\t\t\tEMPLOYEE_ID = ACTIVE.EMPLOYEE_ID\n"
            + "\t\t\tAND EFFECTIVE_TO < NOW()\n"
            + "\t);";
    return jdbcTemplate.query(
        sql,
        rs -> {
          List<BigDecimal> differences = new ArrayList<>();
          while (rs.next()) {
            differences.add(rs.getBigDecimal("SALARY_DIFFERENCE"));
          }
          return differences;
        },
        id);
  }
}
