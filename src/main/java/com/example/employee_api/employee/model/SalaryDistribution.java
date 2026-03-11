package com.example.employee_api.employee.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalaryDistribution {
    private String salaryRange;
    private int countEmployee;
}
