package com.example.employee_api.validation.rules;

import com.example.employee_api.common.exceptions.EmailAlreadyExistException;
import com.example.employee_api.employee.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExistingEmailRuleTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private ExistingEmailRule existingEmailRule;

    @Test
    void checkExistingEmail_emailExists_throwException() {
        String email = "user@gmail.com";
        when(employeeRepository.checkAnExistingEmail(email)).thenReturn(true);
        assertThrows(EmailAlreadyExistException.class,
                () -> existingEmailRule.checkIfEmailPresent(email));
        verify(employeeRepository, times(1)).checkAnExistingEmail(email);
    }

    @Test
    void checkExistingEmail_newEmail_doesNotThrowException() {
        when(employeeRepository.checkAnExistingEmail(anyString())).thenReturn(false);
        assertDoesNotThrow(() -> existingEmailRule.checkIfEmailPresent("user@gmail.com"));
        verify(employeeRepository).checkAnExistingEmail(anyString());
    }
}
