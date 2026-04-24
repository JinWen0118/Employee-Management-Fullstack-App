package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResetPasswordRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();

        dto.setUsername("john");
        dto.setNewPassword("newpass123");

        assertEquals("john", dto.getUsername());
        assertEquals("newpass123", dto.getNewPassword());
    }

    @Test
    void shouldPassValidation_whenValid() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("john");
        dto.setNewPassword("securePass");

        Set<ConstraintViolation<ResetPasswordRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenUsernameBlank() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("");
        dto.setNewPassword("password");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenUsernameNull() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername(null);
        dto.setNewPassword("password");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenPasswordBlank() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("john");
        dto.setNewPassword("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenPasswordNull() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("john");
        dto.setNewPassword(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenBothFieldsNull() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenBothFieldsEmpty() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("");
        dto.setNewPassword("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    // Lombok-generated methods

    @Test
    void shouldTestEqualsAndHashCode() {
        ResetPasswordRequestDto dto1 = new ResetPasswordRequestDto();
        dto1.setUsername("john");
        dto1.setNewPassword("123");

        ResetPasswordRequestDto dto2 = new ResetPasswordRequestDto();
        dto2.setUsername("john");
        dto2.setNewPassword("123");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setNewPassword("different");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString() {
        ResetPasswordRequestDto dto = new ResetPasswordRequestDto();
        dto.setUsername("john");
        dto.setNewPassword("123");

        String result = dto.toString();

        assertTrue(result.contains("john"));
        assertTrue(result.contains("123"));
    }
}