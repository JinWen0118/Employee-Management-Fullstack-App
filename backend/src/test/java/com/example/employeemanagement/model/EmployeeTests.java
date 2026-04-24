package com.example.employeemanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Employee createValidEmployee() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        return new Employee(
                1L,
                "John",
                "Doe",
                "john@example.com",
                dept,
                30
        );
    }

    @Test
    void shouldCreateValidEmployee() {
        Employee emp = createValidEmployee();

        assertEquals("John", emp.getFirstName());
        assertEquals("Doe", emp.getLastName());
        assertEquals("john@example.com", emp.getEmail());
        assertEquals(30, emp.getAge());
        assertNotNull(emp.getDepartment());
    }

    @Test
    void shouldPassValidation_whenEmployeeValid() {
        Employee emp = createValidEmployee();

        Set<ConstraintViolation<Employee>> violations = validator.validate(emp);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenEmailInvalid() {
        Employee emp = createValidEmployee();
        emp.setEmail("invalid-email");

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenEmailBlank() {
        Employee emp = createValidEmployee();
        emp.setEmail("");

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenFirstNameBlank() {
        Employee emp = createValidEmployee();
        emp.setFirstName("");

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenLastNameBlank() {
        Employee emp = createValidEmployee();
        emp.setLastName("");

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenAgeTooLow() {
        Employee emp = createValidEmployee();
        emp.setAge(15);

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenAgeTooHigh() {
        Employee emp = createValidEmployee();
        emp.setAge(70);

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldFailValidation_whenDepartmentNull() {
        Employee emp = createValidEmployee();
        emp.setDepartment(null);

        assertFalse(validator.validate(emp).isEmpty());
    }

    @Test
    void shouldReturnTrue_whenSameReference() {
        Employee e = createValidEmployee();
        assertEquals(e, e);
    }

    @Test
    void shouldReturnFalse_whenNull() {
        Employee e = createValidEmployee();
        assertNotEquals(e, null);
    }

    @Test
    void shouldReturnFalse_whenComparedWithNull() {
        Employee e = createValidEmployee();

        assertNotEquals(e, null);
    }

    @Test
    void shouldReturnFalse_whenDifferentObjectType() {
        Employee e = createValidEmployee();
        assertNotEquals(e, new Object());
    }

    @Test
    void shouldChangeHashCode_whenFieldsChange() {
        Employee e1 = createValidEmployee();
        Employee e2 = createValidEmployee();

        e2.setEmail("different@email.com");

        assertNotEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Employee e1 = createValidEmployee();
        Employee e2 = createValidEmployee();

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());

        e2.setId(2L);
        assertNotEquals(e1, e2);

        assertNotEquals(e1, null);
        assertNotEquals(e1, new Object());
    }

    @Test
    void shouldTestToString() {
        Employee emp = createValidEmployee();

        String result = emp.toString();

        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("john@example.com"));
    }

    @Test
    void shouldTestAllArgsConstructor() {
        Department dept = new Department(1L, "IT", null);

        Employee emp = new Employee(
                2L,
                "Alice",
                "Smith",
                "alice@example.com",
                dept,
                25
        );

        assertEquals(2L, emp.getId());
        assertEquals("Alice", emp.getFirstName());
        assertEquals("Smith", emp.getLastName());
    }
}