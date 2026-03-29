package com.example.employee_api.employee.mapper;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.dto.EmployeePutDTO;
import com.example.employee_api.employee.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
  // Mapping EmployeeCreateDTO to Employee entity when create a new employee
  public Employee toEmployee(EmployeeCreateDTO employeeCreateDTO) {
    Employee employee = new Employee();
    employee.setName(employeeCreateDTO.getName());
    employee.setSurname(employeeCreateDTO.getSurname());
    employee.setEmail(employeeCreateDTO.getEmail());
    employee.setBirth(employeeCreateDTO.getBirth());
    return employee;
  }


  // Used to update an employee
  public Employee toEmployee(
      int id, EmployeePutDTO employeePutDTO, int departmentId, int positionId) {
    Employee employee = new Employee();
    employee.setId(id);
    employee.setName(employeePutDTO.getName());
    employee.setSurname(employeePutDTO.getSurname());
    employee.setEmail(employeePutDTO.getEmail());
    employee.setDepartmentId(departmentId);
    employee.setPositionId(positionId);
    employee.setBirth(employeePutDTO.getBirth());
    return employee;
  }
}
