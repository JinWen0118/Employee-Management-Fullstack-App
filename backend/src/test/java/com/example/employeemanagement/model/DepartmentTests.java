package com.example.employeemanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartmentTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidDepartment() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        assertEquals(1L, dept.getId());
        assertEquals("IT", dept.getName());
    }

    @Test
    void shouldPassValidation_whenNameIsValid() {
        Department dept = new Department();
        dept.setName("Finance");

        Set<ConstraintViolation<Department>> violations = validator.validate(dept);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameIsBlank() {
        Department dept = new Department();
        dept.setName("");

        Set<ConstraintViolation<Department>> violations = validator.validate(dept);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameIsNull() {
        Department dept = new Department();
        dept.setName(null);

        Set<ConstraintViolation<Department>> violations = validator.validate(dept);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleEmployeeRelationship() {
        Department dept = new Department();
        dept.setName("HR");

        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setEmail("john@example.com");
        emp.setAge(30);
        emp.setDepartment(dept);

        List<Employee> employees = new ArrayList<>();
        employees.add(emp);

        dept.setEmployees(employees);

        assertEquals(1, dept.getEmployees().size());
        assertEquals(dept, emp.getDepartment());
    }

    @Test
    void shouldHandleEmptyEmployeeList() {
        Department dept = new Department();
        dept.setEmployees(new ArrayList<>());

        assertTrue(dept.getEmployees().isEmpty());
    }

    @Test
    void shouldAllowNullEmployeesList() {
        Department dept = new Department();

        assertNull(dept.getEmployees());
    }

    @Test
    void shouldTestAllArgsConstructor() {
        List<Employee> employees = new ArrayList<>();
        Department dept = new Department(1L, "IT", employees);

        assertEquals(1L, dept.getId());
        assertEquals("IT", dept.getName());
        assertEquals(employees, dept.getEmployees());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Department d1 = new Department(1L, "IT", null);
        Department d2 = new Department(1L, "IT", null);
        Department d3 = new Department(2L, "HR", null);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());

        assertNotEquals(d1, d3);
        assertNotEquals(d1, null);
        assertNotEquals(d1, new Object());
    }

    @Test
    void shouldTestToString() {
        Department dept = new Department(1L, "Finance", null);

        String result = dept.toString();

        assertTrue(result.contains("Finance"));
        assertTrue(result.contains("1"));
    }
}