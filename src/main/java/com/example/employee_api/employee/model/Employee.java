package com.example.employee_api.employee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Employee {
    private int id;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime hireDate;
    private int departmentId;
    private int positionId;
    private LocalDate birth;
    private int createdBy;
    private LocalDateTime createdAt;
    private boolean isActive;
    private int updatedBy;
    private LocalDateTime updatedAt;
    private int deletedBy;
    private LocalDateTime deletedAt;
}
