package com.example.employee_api.salary.mapper;


import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.salary.dto.SalaryCreateDTO;
import com.example.employee_api.salary.dto.SalaryHistoryResponseDTO;
import com.example.employee_api.salary.dto.SalaryPatchDTO;
import com.example.employee_api.salary.dto.SalaryResponseDTO;
import com.example.employee_api.salary.model.Salary;
import org.springframework.stereotype.Component;

@Component
public class SalaryMapper {
    //Used when Mapping EmployeeCreateDTO salary fields to Salary entity
    public Salary toSalary(EmployeeCreateDTO employeeCreateDTO) {
        Salary salary = new Salary();
        salary.setSalary(employeeCreateDTO.getSalary());
        salary.setCurrency(employeeCreateDTO.getCurrency());
        salary.setEffectiveTo(employeeCreateDTO.getEffectiveTo());
        return salary;
    }

    public Salary toSalary(SalaryCreateDTO salaryCreateDTO) {
        Salary salary = new Salary();
        salary.setSalary(salaryCreateDTO.getSalary());
        salary.setCurrency(salaryCreateDTO.getCurrency());
        salary.setEffectiveTo(salaryCreateDTO.getEffectiveTo());
        salary.setEmployeeId(salaryCreateDTO.getEmployeeId());
        return salary;
    }

    public SalaryResponseDTO toSalaryResponseDTO(Salary salary) {
        SalaryResponseDTO salaryResponseDTO = new SalaryResponseDTO();
        salaryResponseDTO.setId(salary.getId());
        salaryResponseDTO.setSalary(salary.getSalary());
        salaryResponseDTO.setCurrency(salary.getCurrency());
        salaryResponseDTO.setEffectiveFrom(salary.getEffectiveFrom());
        salaryResponseDTO.setEffectiveTo(salary.getEffectiveTo());
        salaryResponseDTO.setEmployeeId(salary.getEmployeeId());
        return salaryResponseDTO;
    }

    public Salary toSalary(SalaryPatchDTO salaryPatchDTO) {
        Salary salary = new Salary();
        if (salaryPatchDTO.getSalary() != null) {
            salary.setSalary(salaryPatchDTO.getSalary());
        }
        if (salaryPatchDTO.getEffectiveTo() != null) {
            salary.setEffectiveTo(salaryPatchDTO.getEffectiveTo());
        }
        return salary;
    }

    public SalaryHistoryResponseDTO toSalaryHistoryResponseDTO(Salary salary) {
        SalaryHistoryResponseDTO historyResponseDTO = new SalaryHistoryResponseDTO();
        historyResponseDTO.setId(salary.getId());
        historyResponseDTO.setSalary(salary.getSalary());
        historyResponseDTO.setCurrency(salary.getCurrency());
        historyResponseDTO.setEffectiveFrom(salary.getEffectiveFrom());
        historyResponseDTO.setEffectiveTo(salary.getEffectiveTo());
        historyResponseDTO.setEmployeeId(salary.getEmployeeId());
        return historyResponseDTO;
    }
}
