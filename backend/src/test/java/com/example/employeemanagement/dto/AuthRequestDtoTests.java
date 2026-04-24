package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        AuthRequestDto dto = new AuthRequestDto();

        dto.setUsername("john");
        dto.setPassword("123456");

        assertEquals("john", dto.getUsername());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    void shouldPassValidation_whenValid() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("john");
        dto.setPassword("password");

        Set<ConstraintViolation<AuthRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenUsernameBlank() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("");
        dto.setPassword("password");

        Set<ConstraintViolation<AuthRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenPasswordBlank() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("john");
        dto.setPassword("");

        Set<ConstraintViolation<AuthRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenUsernameNull() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername(null);
        dto.setPassword("password");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenPasswordNull() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("john");
        dto.setPassword(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenBothFieldsBlank() {
        AuthRequestDto dto = new AuthRequestDto();

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenBothFieldsNull() {
        AuthRequestDto dto = new AuthRequestDto();

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenBothFieldsEmpty() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("");
        dto.setPassword("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        AuthRequestDto dto1 = new AuthRequestDto();
        dto1.setUsername("john");
        dto1.setPassword("123");

        AuthRequestDto dto2 = new AuthRequestDto();
        dto2.setUsername("john");
        dto2.setPassword("123");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setPassword("different");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setUsername("john");
        dto.setPassword("123");

        String result = dto.toString();

        assertTrue(result.contains("john"));
        assertTrue(result.contains("123"));
    }
}