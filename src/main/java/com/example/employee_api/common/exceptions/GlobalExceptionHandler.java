package com.example.employee_api.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseApiException.class)

    public ResponseEntity<ErrorResponse> handleBusinessExceptionsForUser(BaseApiException baseApiException,
                                                                         HttpServletRequest httpServletRequest) {
        log.warn(baseApiException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(baseApiException.getStatus().value())
                .error(baseApiException.getStatus().getReasonPhrase())
                .errorCode(baseApiException.getErrorCode())
                .message(baseApiException.getMessage()).path(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(baseApiException.getStatus()).body(errorResponse);
    }
}
