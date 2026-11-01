package com.example.employee_api.validation;

public interface Validator<T> {
    void validate(T t);
}
