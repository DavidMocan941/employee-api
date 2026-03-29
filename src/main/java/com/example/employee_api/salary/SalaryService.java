package com.example.employee_api.salary;
import com.example.employee_api.common.exceptions.*;
import com.example.employee_api.employee.EmployeeRepository;
import com.example.employee_api.salary.dto.SalaryCreateDTO;
import com.example.employee_api.salary.dto.SalaryHistoryResponseDTO;
import com.example.employee_api.salary.dto.SalaryPatchDTO;
import com.example.employee_api.salary.dto.SalaryResponseDTO;
import com.example.employee_api.salary.mapper.SalaryMapper;
import com.example.employee_api.salary.model.Salary;
import com.example.employee_api.salary.model.SalaryEndDate;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalaryService {
  private final SalaryRepository salaryRepository;
  private final SalaryMapper salaryMapper;
  private final EmployeeRepository employeeRepository;

  @Transactional
  public SalaryResponseDTO addSalary(SalaryCreateDTO salaryCreateDTO) {
    log.info(
        "Checking if exists in salary entity employee id: {}", salaryCreateDTO.getEmployeeId());
    if (!salaryRepository.getEmployeesId().contains(salaryCreateDTO.getEmployeeId())) {
      throw new EmployeeNotFoundException(salaryCreateDTO.getEmployeeId());
    }
    SalaryEndDate salaryEndDate = new SalaryEndDate();
    log.debug("Finding the end date of the current salary");
    salaryRepository.findCurrentSalaryEndDate(salaryCreateDTO.getEmployeeId(), salaryEndDate);
    LocalDate effectiveFrom =
        Optional.ofNullable(salaryCreateDTO.getEffectiveFrom()).orElse(LocalDate.now());
    log.info("Checking on an active salary");
    if (salaryEndDate.getEffectiveTo() == null) {
      salaryEndDate.setEffectiveTo(effectiveFrom.minusDays(1));
      log.info("Checking data range of the current salary");
      if (salaryEndDate
          .getEffectiveTo()
          .isBefore(salaryRepository.findCurrentSalaryStartDate(salaryCreateDTO.getEmployeeId()))) {
        throw new InvalidDataRangeException();
      }
    } else if (salaryEndDate.getExists() == false) {
      throw new NoActiveSalaryException();
    }
    log.info("Checking if overlapping happens");
    if (effectiveFrom.isBefore(salaryEndDate.getEffectiveTo())) {
      throw new NewOverlappingException();
    }
    log.info("Checking data range of the future salary");
    if (salaryCreateDTO.getEffectiveTo() != null
        && salaryCreateDTO.getEffectiveTo().isBefore(effectiveFrom)) {
      throw new InvalidDataRangeException();
    }
    salaryRepository.insertPastSalaryEndDate(salaryEndDate.getId(), salaryEndDate.getEffectiveTo());
    log.info("Mapping SalaryCreateDTO to Salary entity");
    Salary salary = salaryMapper.toSalary(salaryCreateDTO);
    salary.setEffectiveFrom(effectiveFrom);
    log.info("Inserting a new salary");
    int key = salaryRepository.insertNewSalary(salary);
    log.info("Mapping Salary entity to SalaryResponseDTO");
    SalaryResponseDTO salaryResponse =
        salaryMapper.toSalaryResponseDTO(salaryRepository.getSalaryById(key));
    return salaryResponse;
  }

  public SalaryResponseDTO updateSalary(int employeeId, SalaryPatchDTO salaryPatchDTO) {
    log.debug("Checking if exists an active salary for employee id: {}", employeeId);
    salaryRepository.checkIfEmployeeHasActiveSalary(employeeId);
    Integer id = null;
    log.info("Mapping salary entity to SalaryPatchDTO");
    Salary salary = salaryMapper.toSalary(salaryPatchDTO);
    log.info("Validating data");

    if (salaryPatchDTO.getEffectiveTo() != null) {
      if (salaryPatchDTO.getEffectiveTo().isBefore(LocalDate.now())) {
        throw new InvalidDataRangeException();
      }
      log.info("updating salary end date for employee :{}", employeeId);
      id = salaryRepository.updateEffectiveTo(employeeId, salary);
    }
    if (salaryPatchDTO.getSalary() != null) {
      log.info("updating salary for employee :{}", employeeId);
      id = salaryRepository.updateEmployeeSalary(employeeId, salary);
    }
    log.info("Mapping salary to SalaryResponseDTO");
    SalaryResponseDTO response =
        salaryMapper.toSalaryResponseDTO(salaryRepository.getSalaryById(id));
    return response;
  }

  public List<SalaryHistoryResponseDTO> getSalaryHistory(int id) {
    employeeRepository.checkIfEmployeeExists(id);
    List<SalaryHistoryResponseDTO> list =
        salaryRepository.findEmployeeSalaryHistory(id).stream()
            .sorted(Comparator.comparing(Salary::getSalary))
            .map(salaryMapper::toSalaryHistoryResponseDTO)
            .toList();
    return list;
  }
}
