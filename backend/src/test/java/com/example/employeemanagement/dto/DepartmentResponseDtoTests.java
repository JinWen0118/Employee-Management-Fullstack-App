package com.example.employeemanagement.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DepartmentResponseDtoTest {

    @Test
    void shouldSetAndGetFields() {
        DepartmentResponseDto dto = new DepartmentResponseDto();

        dto.setId(1L);
        dto.setName("IT");
        dto.setEmployeeCount(5);

        assertEquals(1L, dto.getId());
        assertEquals("IT", dto.getName());
        assertEquals(5, dto.getEmployeeCount());
    }

    @Test
    void shouldHandleDefaultValues() {
        DepartmentResponseDto dto = new DepartmentResponseDto();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertEquals(0, dto.getEmployeeCount()); // default int
    }

    // Lombok-generated methods

    @Test
    void shouldTestEqualsAndHashCode() {
        DepartmentResponseDto dto1 = new DepartmentResponseDto();
        dto1.setId(1L);
        dto1.setName("IT");
        dto1.setEmployeeCount(10);

        DepartmentResponseDto dto2 = new DepartmentResponseDto();
        dto2.setId(1L);
        dto2.setName("IT");
        dto2.setEmployeeCount(10);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setEmployeeCount(5);
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void shouldTestToString() {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(2L);
        dto.setName("HR");
        dto.setEmployeeCount(3);

        String result = dto.toString();

        assertTrue(result.contains("HR"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }
}