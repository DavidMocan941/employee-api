package com.example.employee_api.common.audit;

import com.example.employee_api.employee.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmployeeAuditService {
    final Integer userId = 1;

    public void setAuditFieldsWhenCreate(Employee employee) {
        EmployeeAudit audit = new EmployeeAudit();
        audit.setCreatedAt(LocalDateTime.now());
        audit.setCreatedBy(userId);
        employee.setCreatedBy(audit.getCreatedBy());
        employee.setCreatedAt(audit.getCreatedAt());
    }

    public void setAuditFieldsWhenUpdate(Employee employee) {
        EmployeeAudit audit = new EmployeeAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(userId);
        employee.setUpdatedBy(audit.getUpdatedBy());
        employee.setUpdatedAt(audit.getUpdatedAt());
    }

    public EmployeeAudit setAuditFieldsWhenDelete(Employee employee) {
        EmployeeAudit audit = new EmployeeAudit();
        audit.setDeletedAt(LocalDateTime.now());
        audit.setDeletedBy(userId);
        employee.setDeletedAt(audit.getDeletedAt());
        employee.setDeletedBy(audit.getDeletedBy());
        return audit;
    }

}
