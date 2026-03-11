package com.example.employee_api.employee.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EmployeeCreateDTO {
    @NotBlank(message = "Required field")
    private String name;
    @NotBlank(message = "Required field")
    private String surname;
    @NotBlank(message = "Required field")
    @Email(message = "Must be a valid email")
    private String email;
    @NotBlank(message = "Required field")
    private String departmentName;
    @NotBlank(message = "Required field")
    private String positionName;
    @NotNull(message = "Required field")
    @Past(message = "Past date required")
    private LocalDate birth;
    @NotNull(message = "Required field")
    private BigDecimal salary;
    @NotBlank(message = "Required field")
    private String currency;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
