package com.example.employee_api.common;

import com.example.employee_api.common.exceptions.BaseApiException;
import com.example.employee_api.common.exceptions.DepartmentNotFoundException;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.employee.model.Employee;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;

import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class EntityValidator<T> implements Validator {





    @Override
    public void validate() {

    }

}
