package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmployeeRequestDto createValidDto() {
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setAge(30);

        EmployeeRequestDto.DepartmentRef dept = new EmployeeRequestDto.DepartmentRef();
        dept.setId(1L);

        dto.setDepartment(dept);
        return dto;
    }

    @Test
    void shouldPassValidation_whenValid() {
        EmployeeRequestDto dto = createValidDto();

        Set<ConstraintViolation<EmployeeRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    

    // FIELD VALIDATION TESTS

    @Test
    void shouldFail_whenFirstNameBlank() {
        EmployeeRequestDto dto = createValidDto();
        dto.setFirstName("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenLastNameBlank() {
        EmployeeRequestDto dto = createValidDto();
        dto.setLastName("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenEmailBlank() {
        EmployeeRequestDto dto = createValidDto();
        dto.setEmail("");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenEmailInvalid() {
        EmployeeRequestDto dto = createValidDto();
        dto.setEmail("invalid-email");

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenAgeTooLow() {
        EmployeeRequestDto dto = createValidDto();
        dto.setAge(10);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenAgeTooHigh() {
        EmployeeRequestDto dto = createValidDto();
        dto.setAge(70);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFail_whenDepartmentNull() {
        EmployeeRequestDto dto = createValidDto();
        dto.setDepartment(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    // NESTED VALIDATION

    @Test
    void shouldFail_whenDepartmentIdNull() {
        EmployeeRequestDto dto = createValidDto();
        dto.getDepartment().setId(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    // Lombok coverage boosters

    @Test
    void shouldTestEqualsAndHashCode() {
        EmployeeRequestDto dto1 = createValidDto();
        EmployeeRequestDto dto2 = createValidDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setEmail("different@email.com");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString() {
        EmployeeRequestDto dto = createValidDto();

        String result = dto.toString();

        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("john@example.com"));
    }

    // Nested class coverage

    @Test
    void shouldTestDepartmentRef() {
        EmployeeRequestDto.DepartmentRef ref = new EmployeeRequestDto.DepartmentRef();
        ref.setId(5L);

        assertEquals(5L, ref.getId());

        EmployeeRequestDto.DepartmentRef ref2 = new EmployeeRequestDto.DepartmentRef();
        ref2.setId(5L);

        assertEquals(ref, ref2);
        assertEquals(ref.hashCode(), ref2.hashCode());
    }

    // Fix - increase branch coverage

    @Test
    void shouldFailValidation_whenAllFieldsInvalidTogether() {
        EmployeeRequestDto dto = new EmployeeRequestDto();

        Set<ConstraintViolation<EmployeeRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenAllFieldsNull() {
        EmployeeRequestDto dto = new EmployeeRequestDto();

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenMultipleFieldsInvalidTogether() {
        EmployeeRequestDto dto = createValidDto();

        dto.setFirstName("");
        dto.setLastName("");
        dto.setEmail("invalid");
        dto.setAge(10);
        dto.setDepartment(null);

        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidation_whenNestedDepartmentIdIsNull() {
        EmployeeRequestDto dto = createValidDto();

        dto.getDepartment().setId(null);

        assertFalse(validator.validate(dto).isEmpty());
    }
}