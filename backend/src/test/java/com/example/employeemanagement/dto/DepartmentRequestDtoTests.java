package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartmentRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetName() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setName("IT");
        assertEquals("IT", dto.getName());
    }

    @Test
    void shouldPassValidation_whenNameValid() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setName("Finance");
        Set<ConstraintViolation<DepartmentRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameBlank() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setName("");
        Set<ConstraintViolation<DepartmentRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameNull() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameEmpty() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setName("");
        assertFalse(validator.validate(dto).isEmpty());
    }

    // Lombok coverage boosters

    @Test
    void shouldTestEqualsAndHashCode() {
        DepartmentRequestDto dto1 = new DepartmentRequestDto();
        dto1.setName("IT");
        DepartmentRequestDto dto2 = new DepartmentRequestDto();
        dto2.setName("IT");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName("HR");
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setName("Marketing");
        String result = dto.toString();
        assertTrue(result.contains("Marketing"));
    }
}