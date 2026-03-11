package com.example.employee_api.salary.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SalaryEndDate {
    private int id;
    private LocalDate effectiveTo;
    private Boolean exists;
}
