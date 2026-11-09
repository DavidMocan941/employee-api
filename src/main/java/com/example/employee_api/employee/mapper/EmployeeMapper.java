package com.example.employee_api.employee.mapper;

import com.example.employee_api.employee.dto.EmployeeCreateDto;
import com.example.employee_api.employee.dto.EmployeePutDto;
import com.example.employee_api.employee.dto.EmployeeResponseDto;
import com.example.employee_api.employee.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

  public Employee toEmployee(
      EmployeeCreateDto employeeCreateDTO, int departmentId, int positionId) {
    Employee employee = new Employee();
    employee.setName(employeeCreateDTO.getName());
    employee.setSurname(employeeCreateDTO.getSurname());
    employee.setEmail(employeeCreateDTO.getEmail());
    employee.setBirth(employeeCreateDTO.getBirth());
    employee.setPositionId(positionId);
    employee.setDepartmentId(departmentId);
    return employee;
  }

  public EmployeeResponseDto toEmployeeResponseDTO(Employee employee) {
    EmployeeResponseDto dto = new EmployeeResponseDto();
    dto.setId(employee.getId());
    dto.setName(employee.getName());
    dto.setSurname(employee.getSurname());
    dto.setEmail(employee.getEmail());
    dto.setHireDate(employee.getHireDate());
    dto.setDepartmentId(employee.getDepartmentId());
    dto.setPositionId(employee.getPositionId());
    dto.setBirth(employee.getBirth());
    return dto;
  }

  public Employee toEmployee(
      int id, EmployeePutDto employeePutDTO, int departmentId, int positionId) {
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

  // Getting only employee
  /*public EmployeeResponseDTO toEmployeeResponseDTO(Employee employee) {
    EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
    employeeResponseDTO.setId(employee.getId());
    employeeResponseDTO.setName(employee.getName());
    employeeResponseDTO.setSurname(employee.getSurname());
    employeeResponseDTO.setEmail(employee.getEmail());
    employeeResponseDTO.setHireDate(employee.getHireDate());
    employeeResponseDTO.setDepartmentId(employee.getDepartmentId());
    employeeResponseDTO.setPositionId(employee.getPositionId());
    employeeResponseDTO.setBirth(employee.getBirth());
    return employeeResponseDTO;
  }*/
}
