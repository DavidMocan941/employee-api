package com.example.employee_api.employee.validation.rules;

public interface RuleValidator<T> {
  void validate(T t);
}
