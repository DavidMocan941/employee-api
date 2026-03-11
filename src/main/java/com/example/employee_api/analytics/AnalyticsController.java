package com.example.employee_api.analytics;

import com.example.employee_api.analytics.service.CompanyAnalyticsService;
import com.example.employee_api.analytics.service.DepartmentAnalyticsService;
import com.example.employee_api.analytics.service.EmployeeAnalyticsService;
import com.example.employee_api.analytics.service.SalaryAnalyticsService;
import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.department.dto.DepartmentSalaryBudgetDTO;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.dto.TopPaidEmployeeResponseDTO;
import com.example.employee_api.employee.model.SalaryDistribution;
import com.example.employee_api.salary.dto.SalaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {
    private final EmployeeAnalyticsService employeeAnalyticsService;
    private final DepartmentAnalyticsService departmentAnalyticsService;
    private final CompanyAnalyticsService companyAnalyticsService;
    private final SalaryAnalyticsService salaryAnalyticsService;

    @GetMapping("/employee/{id}/salary/average")
    public ResponseEntity<Map<Object, Object>> getAvgSalaryOfEmployee(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal avg = employeeAnalyticsService.findEmployeeAvgSalary(id);
        map.put("Message:", "Got avg salary for employee with id:" + id);
        map.put("Average Salary:", avg);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/employee/{id}/salary/highest")
    public ResponseEntity<Map<Object, Object>> getHighestSalaryOfEmployee(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal highest = employeeAnalyticsService.findEmployeeHighestSalary(id);
        map.put("Message:", "Got highest salary for employee with id:" + id);
        map.put("Highest Salary:", highest);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/employee/{id}/salary/lowest")
    public ResponseEntity<Map<Object, Object>> getLowestSalaryOfEmployee(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal lowest = employeeAnalyticsService.findEmployeeLowestSalary(id);
        map.put("Message:", "Got lowest salary for employee with id:" + id);
        map.put("Lowest Salary:", lowest);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/employee/{id}/salary/growth")
    public ResponseEntity<Map<Object, Object>> getSalaryGrowthOfEmployee(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        List<BigDecimal> growth = employeeAnalyticsService.findEmployeeSalaryGrowth(id);
        map.put("Message:", "Got salary growth for employee with id:" + id);
        map.put("Salary growth:", growth);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/employee/{id}/salary/change-count")
    public ResponseEntity<Map<Object, Object>> getChangeSalaryCountOfEmployee(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        Integer count = employeeAnalyticsService.findEmployeeChangeSalaryCount(id);
        map.put("Message:", "Got change salary count for employee with id:" + id);
        map.put("Salary Change Count:", count);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/department/{id}/salary/average")
    public ResponseEntity<Map<Object, Object>> getSalaryAverageOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal average = departmentAnalyticsService.findAvgSalaryForDepartment(id);
        map.put("Message:", "Got the average salary for department with id:" + id);
        map.put("Average: ", average);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/department/{id}/salary/budget")
    public ResponseEntity<Map<Object, Object>> getSalaryBudgetOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal budget = departmentAnalyticsService.findSalaryBudgedForDepartment(id);
        map.put("Message:", "Got the budget salary for department with id:" + id);
        map.put("Budget: ", budget);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/department/{id}/employee/highest-paid")
    public ResponseEntity<Map<Object, Object>> getHighestPaidEmployeeOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        EmployeeResponseDTO responseDTO = departmentAnalyticsService.findHighestPaidEmployeeForDepartment(id);
        map.put("Message:", "Got the highest paid employee for department with id:" + id);
        map.put("Employee: ", responseDTO);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/department/{id}/employee/count")
    public ResponseEntity<Map<Object, Object>> getCountEmployeeOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        Integer count = departmentAnalyticsService.findEmployeeCountForDepartment(id);
        map.put("Message:", "Got the employee number for department with id:" + id);
        map.put("Count: ", count);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/department/{id}/employee/salary-distribution")
    public ResponseEntity<Map<Object, Object>> getSalaryDistributionOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        List<SalaryDistribution> distribution = departmentAnalyticsService.findSalaryDistributionForDepartment(id);
        map.put("Message:", "Got the salary distribution for department with id:" + id);
        map.put("Salary Distribution: ", distribution);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/salary/total-budget")
    public ResponseEntity<Map<Object, Object>> getTotalSalaryBudgetOfCompany() {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal salaryBudget = companyAnalyticsService.findCompanyTotalSalaryBudged();
        map.put("Message:", "Got the total salary budget of the company");
        map.put("Budget: ", salaryBudget);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/salary/average")
    public ResponseEntity<Map<Object, Object>> getAverageSalaryOfCompany() {
        Map<Object, Object> map = new HashMap<>();
        BigDecimal averageSalary = companyAnalyticsService.findCompanyAverageSalary();
        map.put("Message:", "Got the average salary of the company");
        map.put("Average Salary: ", averageSalary);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/employees/top-paid")
    public ResponseEntity<Map<Object, Object>> getTopPaidEmployeesOfCompany() {
        Map<Object, Object> map = new HashMap<>();
        List<TopPaidEmployeeResponseDTO> topPaidEmployees = companyAnalyticsService.findCompanyTopPaidEmployees();
        map.put("Message:", "Got the top paid employees of the company");
        map.put("Top paid employees: ", topPaidEmployees);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/department/highest-salary-budget")
    public ResponseEntity<Map<Object, Object>> getDepartmentWithHighestSalaryBudget() {
        Map<Object, Object> map = new HashMap<>();
        DepartmentSalaryBudgetDTO highestSalaryBudget = companyAnalyticsService.findDepartmentWithHighestSalaryBudget();
        map.put("Message:", "Got the department with highest salary budget");
        map.put("Salary Budget:", highestSalaryBudget);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/department/most-employees")
    public ResponseEntity<Map<Object, Object>> getDepartmentWithMostEmployees() {
        Map<Object, Object> map = new HashMap<>();
        DepartmentResponseDTO department = companyAnalyticsService.findDepartmentWithMostEmployees();
        map.put("Message:", "Got the department with the most employees");
        map.put("Department:", department);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/employee/salary-increase-this-year")
    public ResponseEntity<Map<Object, Object>> getEmployeesWithSalaryIncreaseThisYear() {
        Map<Object, Object> map = new HashMap<>();
        List<EmployeeResponseDTO> employees = salaryAnalyticsService.findEmployeesWithSalaryIncreaseThisYear();
        map.put("Message:", "Got employees with salary increase this year");
        map.put("Employees:", employees);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/employee/salary-decrease")
    public ResponseEntity<Map<Object, Object>> getEmployeesWithSalaryDecrease() {
        Map<Object, Object> map = new HashMap<>();
        List<EmployeeResponseDTO> employees = salaryAnalyticsService.findEmployeesWithSalaryDecrease();
        map.put("Message:", "Got employees with salary decrease");
        map.put("Employees:", employees);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/employee/salary-changes-in-months/{months}")
    public ResponseEntity<Map<Object, Object>> getEmployeesWithSalaryDecrease(@PathVariable int months) {
        Map<Object, Object> map = new HashMap<>();
        List<SalaryResponseDTO> salaries = salaryAnalyticsService.findSalaryChangesInLastMonths(months);
        map.put("Message:", "Got salary changes in last months");
        map.put("Salaries:", salaries);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/company/department/salary-growth/{id}")
    public ResponseEntity<Map<Object, Object>> getSalaryGrowthOfDepartment(@PathVariable int id) {
        Map<Object, Object> map = new HashMap<>();
        List<BigDecimal> differences = salaryAnalyticsService.findDepartmentSalaryGrowth(id);
        map.put("Message:", "God salary differences for department with id:" + id);
        map.put("Salaries:", differences);
        return ResponseEntity.ok().body(map);
    }
}

