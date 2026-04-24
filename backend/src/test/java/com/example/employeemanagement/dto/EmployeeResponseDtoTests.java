package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeResponseDtoTest {

    private EmployeeResponseDto createDto() {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setAge(30);

        EmployeeResponseDto.DepartmentDto dept = new EmployeeResponseDto.DepartmentDto();
        dept.setId(10L);
        dept.setName("IT");

        dto.setDepartment(dept);

        return dto;
    }

    @Test
    void shouldSetAndGetFields() {
        EmployeeResponseDto dto = createDto();

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals(30, dto.getAge());
        assertNotNull(dto.getDepartment());
        assertEquals("IT", dto.getDepartment().getName());
    }

    @Test
    void shouldHandleDefaultValues() {
        EmployeeResponseDto dto = new EmployeeResponseDto();

        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertEquals(0, dto.getAge());
        assertNull(dto.getDepartment());
    }

    // Lombok coverage (main DTO)

    @Test
    void shouldTestEqualsAndHashCode_mainDto() {
        EmployeeResponseDto dto1 = createDto();
        EmployeeResponseDto dto2 = createDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setEmail("different@email.com");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString_mainDto() {
        EmployeeResponseDto dto = createDto();

        String result = dto.toString();

        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("john@example.com"));
    }

    // Nested DTO coverage

    @Test
    void shouldTestDepartmentDto() {
        EmployeeResponseDto.DepartmentDto d1 = new EmployeeResponseDto.DepartmentDto();
        d1.setId(1L);
        d1.setName("HR");

        EmployeeResponseDto.DepartmentDto d2 = new EmployeeResponseDto.DepartmentDto();
        d2.setId(1L);
        d2.setName("HR");

        assertEquals(d1.getId(), 1L);
        assertEquals(d1.getName(), "HR");

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());

        d2.setName("Finance");
        assertNotEquals(d1, d2);
    }

    @Test
    void shouldTestToString_departmentDto() {
        EmployeeResponseDto.DepartmentDto dept = new EmployeeResponseDto.DepartmentDto();
        dept.setId(5L);
        dept.setName("Marketing");

        String result = dept.toString();

        assertTrue(result.contains("Marketing"));
        assertTrue(result.contains("5"));
    }
}