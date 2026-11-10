package com.example.employee_api.employee;

import com.example.employee_api.employee.dto.EmployeeCreateDto;
import com.example.employee_api.employee.dto.EmployeePutDto;
import com.example.employee_api.employee.dto.EmployeeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class
EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeCreateDto employeeCreateDTO,
                                                              BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage() + ": " + error.getField())
                    .toList();
            map.put("Error : ", "Validation failed");
            map.put("Cause : ", errors);
            return ResponseEntity.badRequest().body(map);
        }
        EmployeeResponseDto response = employeeService.addEmployeeAndSalary(employeeCreateDTO);
        map.put("Message :", "A new employee added");
        map.put("Employee", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable int id,
                                                              @Valid @RequestBody EmployeePutDto employeePutDTO,
                                                              BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().
                    stream().
                    map(error -> error.getDefaultMessage() + ": " + error.getField()).
                    toList();
            result.put("Result: ", "Validation failed");
            result.put("Message", errors);
            return ResponseEntity.badRequest().body(result);
        }
        EmployeeResponseDto response = employeeService.updateEmployee(id, employeePutDTO);
        result.put("Result", "Successfully updated employee");
        result.put("Message", response);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Map<String, EmployeeResponseDto>> getEmployeeById(@PathVariable int id) {
        Map<String, EmployeeResponseDto> map = new HashMap<>();
        EmployeeResponseDto response = employeeService.fetchEmployee(id);
        map.put("Response", response);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, List<EmployeeResponseDto>>> getEmployees() {
        Map<String, List<EmployeeResponseDto>> map = new HashMap<>();
        List<EmployeeResponseDto> list = employeeService.getAllEmployees();
        map.put("Response", list);
        return ResponseEntity.ok().body(map);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}