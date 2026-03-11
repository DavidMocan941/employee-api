package com.example.employee_api.employee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class EmployeeResponseDTO {
    private int id;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime hireDate;
    private int departmentId;
    private int positionId;
    private LocalDate birth;
}
