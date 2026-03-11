package com.example.employee_api.employee.dto;

import com.example.employee_api.department.dto.DepartmentResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class TopPaidEmployeeResponseDTO {
    private int id;
    private String name;
    private String surname;
    private DepartmentResponseDTO departmentResponseDTO;
    private BigDecimal salary;
}
