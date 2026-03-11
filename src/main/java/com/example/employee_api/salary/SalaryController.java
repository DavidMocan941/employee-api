package com.example.employee_api.salary;

import com.example.employee_api.salary.dto.SalaryCreateDTO;
import com.example.employee_api.salary.dto.SalaryHistoryResponseDTO;
import com.example.employee_api.salary.dto.SalaryPatchDTO;
import com.example.employee_api.salary.dto.SalaryResponseDTO;
import com.example.employee_api.salary.model.Salary;
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
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSalary(@RequestBody @Valid SalaryCreateDTO salaryCreateDTO,
                                                            BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().
                    stream().
                    map(error -> error.getDefaultMessage() + ": " + error.getField()).
                    toList();
            map.put("Message :", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        SalaryResponseDTO response = salaryService.addSalary(salaryCreateDTO);
        map.put("Result :", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @PatchMapping("/update/{employeeId}")
    public ResponseEntity<Map<String, Object>> changeSalary(@PathVariable int employeeId,
                                                            @RequestBody SalaryPatchDTO salaryPatchDTO) {
        Map<String, Object> map = new HashMap<>();
        SalaryResponseDTO response = salaryService.updateSalary(employeeId, salaryPatchDTO);
        map.put("Message: ", "The salary was successfully updated");
        map.put("Result: ", response);
        return ResponseEntity.ok().body(map);

    }

    @GetMapping("history/{id}")
    public ResponseEntity<Map<String, List<SalaryHistoryResponseDTO>>> getSalaryHistoryById(@PathVariable int id) {
        Map<String, List<SalaryHistoryResponseDTO>> map = new HashMap<>();
        List<SalaryHistoryResponseDTO> list = salaryService.getSalaryHistory(id);
        map.put("Result", list);
        return ResponseEntity.ok().body(map);
    }
}
