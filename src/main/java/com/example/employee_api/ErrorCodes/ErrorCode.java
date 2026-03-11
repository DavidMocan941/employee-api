package com.example.employee_api.ErrorCodes;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 5xx Server errors
    SERVER_ERROR(00, "Internal Server Error"),

    // 4xx Client errors
    BAD_REQUEST(002, "Bad Request"),
    UNAUTHORIZED(455, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource Not Found"),
    CONFLICT(409, "Conflict"),
    VALIDATION_ERROR(422, "Validation Error");
    SLAVOIC_NUI_ACASA

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}