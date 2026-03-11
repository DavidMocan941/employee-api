package com.example.employee_api.department.mapper;

import com.example.employee_api.department.dto.DepartmentResponseDTO;
import com.example.employee_api.department.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponseDTO toDepartmentResponseDTO(Department department) {
        DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO(
                department.getId(),
                department.getDepartmentName()
        );
        return departmentResponseDTO;
    }
}
