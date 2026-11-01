package com.example.employee_api.employee;

import com.example.employee_api.common.audit.EmployeeAuditService;
import com.example.employee_api.common.exceptions.*;
import com.example.employee_api.department.DepartmentRepository;
import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.dto.EmployeePutDTO;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
import com.example.employee_api.employee.mapper.EmployeeMapper;
import com.example.employee_api.employee.model.Employee;
import com.example.employee_api.position.PositionRepository;
import com.example.employee_api.salary.SalaryRepository;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import com.example.employee_api.validation.employee.EmployeeCreationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeAuditService auditService;
    private final SalaryMapper salaryMapper;
    private final EmployeeCreationValidator employeeCreationValidator;

    @Transactional
    public EmployeeResponseDTO addEmployeeAndSalary(EmployeeCreateDTO employeeCreateDTO) {

        employeeCreationValidator.validate(employeeCreateDTO);

        int departmentId = departmentResolver(employeeCreateDTO.getDepartmentName());
        int positionId = positionResolver(employeeCreateDTO.getPositionName());

        Employee employee = employeeMapper.toEmployee(employeeCreateDTO, departmentId, positionId);
        int employeeKey = employeeRepository.insertEmployee(employee);

        Salary salary = salaryMapper.toSalary(employeeCreateDTO, employeeKey);
        salaryRepository.insertSalary(salary);


        return employeeMapper.toEmployeeResponseDTO(employee, salary);

    }

    public EmployeeResponseDTO getUpdatedEmployee(int id, EmployeePutDTO employeePutDTO) {
        log.info("Checking if employee exists with id: {}", id);
        boolean empExists = employeeRepository.checkIfEmployeeExists(id);
        if (!empExists) {
            log.warn("Employee not found with id: {}", id);
            throw new EmployeeNotFoundException(id);
        }
        log.info("Checking an existing email: {}", employeePutDTO.getEmail());
        boolean emailExists = employeeRepository.checkAnExistingEmail(employeePutDTO.getEmail());
        if (emailExists) {
            log.warn("Exist an employee with the same email: {}", employeePutDTO.getEmail());
            throw new EmailAlreadyExistException(employeePutDTO.getEmail());
        }
        log.debug("Resolving department: {} and position: {}",
                employeePutDTO.getDepartmentName(), employeePutDTO.getPositionName());
        int departmentId = departmentRepository.findIdByDepartment(employeePutDTO.getDepartmentName()).
                orElseThrow(() -> new DepartmentNotFoundException(employeePutDTO.getDepartmentName()));
        int positionId = positionRepository.findIdByPosition(employeePutDTO.getPositionName()).
                orElseThrow(() -> new PositionNotFoundException(employeePutDTO.getPositionName()));
        log.debug("Mapping EmployeePutDTO to Employee entity");
        Employee employee = employeeMapper.toEmployee(id, employeePutDTO, departmentId, positionId);
        auditService.setAuditFieldsWhenUpdate(employee);
        log.info("Update employee with id: {}", id);
        employeeRepository.fullyUpdateEmployeeById(employee);
        Employee responseEmployee = employeeRepository.findEmployee(id);
        EmployeeResponseDTO responseDTO = employeeMapper.toEmployeeResponseDTO(responseEmployee);
        return responseDTO;
    }

    public EmployeeResponseDTO getEmployee(int id) {
        boolean exists = employeeRepository.checkIfEmployeeExists(id);
        if (!exists) {
            throw new EmployeeNotFoundException(id);
        }
        Employee employee = employeeRepository.findEmployee(id);
        EmployeeResponseDTO responseDTO = employeeMapper.toEmployeeResponseDTO(employee);
        return responseDTO;
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        List<EmployeeResponseDTO> response = employeeRepository.findAllEmployees().
                stream().
                sorted(Comparator.comparing(Employee::getId).reversed()).
                map(employeeMapper::toEmployeeResponseDTO).
                toList();
        return response;
    }

    public void deleteEmployee(int id) {
        boolean employeeExists = employeeRepository.checkIfEmployeeExists(id);
        if (employeeExists) {
            boolean activeSalaryExists = salaryRepository.checkIfEmployeeHasActiveSalary(id);
            if (activeSalaryExists) {
                throw new ActiveSalaryExistsException();
            }
        } else throw new EmployeeNotFoundException(id);
        Employee employee = new Employee();
        auditService.setAuditFieldsWhenDelete(employee);
        employee.setActive(false);
        employeeRepository.deleteEmployeeById(id, employee);
    }

    private int departmentResolver(String department) {
        return departmentRepository.findIdByDepartment(department).
                orElseThrow(() -> new DepartmentNotFoundException(department));
    }

    private int positionResolver(String position) {
        return positionRepository.findIdByPosition(position).
                orElseThrow(() -> new PositionNotFoundException(position));

    }
}

