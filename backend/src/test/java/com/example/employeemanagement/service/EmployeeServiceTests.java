package com.example.employeemanagement.service;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
    }

    // =========================
    // getAllEmployees()
    // =========================

    @Test
    void testGetAllEmployees_ReturnsList() {
        when(employeeRepository.findAllWithDepartments())
                .thenReturn(List.of(employee));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());

        verify(employeeRepository).findAllWithDepartments();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void testGetAllEmployees_EmptyList() {
        when(employeeRepository.findAllWithDepartments())
                .thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.getAllEmployees();

        assertTrue(result.isEmpty());

        verify(employeeRepository).findAllWithDepartments();
    }

    // =========================
    // getEmployeeById()
    // =========================

    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findByIdWithDepartment(1L))
                .thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());

        verify(employeeRepository).findByIdWithDepartment(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findByIdWithDepartment(1L))
                .thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertFalse(result.isPresent());

        verify(employeeRepository).findByIdWithDepartment(1L);
    }

    @Test
    void testGetEmployeeById_NegativeId() {
        when(employeeRepository.findByIdWithDepartment(-1L))
                .thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(-1L);

        assertFalse(result.isPresent());

        verify(employeeRepository).findByIdWithDepartment(-1L);
    }

    @Test
    void testGetEmployeeById_RepositoryThrowsException() {
        when(employeeRepository.findByIdWithDepartment(1L))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
    }

    // =========================
    // saveEmployee()
    // =========================

    @Test
    void testSaveEmployee_Success_WithRefetch() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findByIdWithDepartment(1L))
                .thenReturn(Optional.of(employee));

        Employee result = employeeService.saveEmployee(employee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(employeeRepository).save(employee);
        verify(entityManager).flush();
        verify(entityManager).clear();
        verify(employeeRepository).findByIdWithDepartment(1L);
    }

    @Test
    void testSaveEmployee_FallbackToSaved_WhenRefetchFails() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findByIdWithDepartment(1L))
                .thenReturn(Optional.empty());

        Employee result = employeeService.saveEmployee(employee);

        assertEquals(employee, result);

        verify(employeeRepository).save(employee);
        verify(entityManager).flush();
        verify(entityManager).clear();
    }

    @Test
    void testSaveEmployee_NullInput() {
        assertThrows(Exception.class, () -> {
            employeeService.saveEmployee(null);
        });
    }

    @Test
    void testSaveEmployee_RepositoryThrowsException() {
        when(employeeRepository.save(employee))
                .thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        verify(entityManager, never()).flush();
        verify(entityManager, never()).clear();
    }

    // =========================
    // deleteEmployee()
    // =========================

    @Test
    void testDeleteEmployee_Success() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NonExistingId() {
        doNothing().when(employeeRepository).deleteById(999L);

        employeeService.deleteEmployee(999L);

        verify(employeeRepository).deleteById(999L);
    }

    @Test
    void testDeleteEmployee_RepositoryThrowsException() {
        doThrow(new RuntimeException("Delete failed"))
                .when(employeeRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        verify(employeeRepository).deleteById(1L);
    }
}