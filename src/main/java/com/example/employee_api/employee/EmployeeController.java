package com.example.employee_api.employee;

import com.example.employee_api.employee.dto.EmployeeCreateDTO;
import com.example.employee_api.employee.dto.EmployeePutDTO;
import com.example.employee_api.employee.dto.EmployeeResponseDTO;
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
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeCreateDTO employeeCreateDTO,
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
        EmployeeResponseDTO response = employeeService.addEmployeeAndSalary(employeeCreateDTO);
        map.put("Message :", "A new employee added");
        map.put("Employee", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable int id,
                                                              @Valid @RequestBody EmployeePutDTO employeePutDTO,
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
        EmployeeResponseDTO response = employeeService.getUpdatedEmployee(id, employeePutDTO);
        result.put("Result", "Successfully updated employee");
        result.put("Message", response);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Map<String, EmployeeResponseDTO>> getEmployeeById(@PathVariable int id) {
        Map<String, EmployeeResponseDTO> map = new HashMap<>();
        EmployeeResponseDTO response = employeeService.getEmployee(id);
        map.put("Response", response);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, List<EmployeeResponseDTO>>> getEmployees() {
        Map<String, List<EmployeeResponseDTO>> map = new HashMap<>();
        List<EmployeeResponseDTO> list = employeeService.getAllEmployees();
        map.put("Response", list);
        return ResponseEntity.ok().body(map);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}