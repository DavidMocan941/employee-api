package com.example.employee_api.department;

import com.example.employee_api.common.exceptions.DepartmentNotDeletedException;
import com.example.employee_api.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public int deleteDepartment(int id) {
        int count = employeeRepository.countEmployeesOfDepartment(id);
        if (count != 0) {
            throw new DepartmentNotDeletedException(id);
        }
        log.info("Department was successfully deleted");
        return departmentRepository.deleteDepartmentFromDb(id);
    }
}
