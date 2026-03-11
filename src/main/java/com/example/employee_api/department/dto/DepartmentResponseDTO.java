package com.example.employee_api.department.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DepartmentResponseDTO {
    private final int id;
    private final String departmentName;
}
