package com.example.employee_api.common.audit;

import com.example.employee_api.salary.model.Salary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SalaryAuditService {
    public void setAuditFieldsWhenUpdate(Salary salary) {
        Integer userId = 1;
        SalaryAudit audit = new SalaryAudit();
        audit.setUpdatedAt(LocalDateTime.now());
        audit.setUpdatedBy(userId);
        salary.setUpdatedAt(audit.getUpdatedAt());
        salary.setUpdatedBy(audit.getUpdatedBy());
    }

    public void setAuditFieldsWhenCreate(Salary salary) {
        Integer userId = 1;
        SalaryAudit audit = new SalaryAudit();
        audit.setCreatedAt(LocalDateTime.now());
        audit.setCreatedBy(userId);
        salary.setCreatedBy(audit.getCreatedBy());
        salary.setCreatedAt(audit.getCreatedAt());
    }

}
