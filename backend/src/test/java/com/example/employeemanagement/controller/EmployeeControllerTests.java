package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.security.JwtTokenUtil;
import com.example.employeemanagement.security.JwtRequestFilter;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    // ✅ SECURITY FIX (FULL CHAIN MOCKED)
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEmployees() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setFirstName("John");

        when(employeeService.getAllEmployees()).thenReturn(List.of(emp));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetEmployeeById_Found() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);

        when(employeeService.getEmployeeById(1L))
                .thenReturn(Optional.of(emp));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmployee() throws Exception {
        Department dept = new Department();
        dept.setId(1L);

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(dept));

        Employee saved = new Employee();
        saved.setId(1L);

        when(employeeService.saveEmployee(any(Employee.class)))
                .thenReturn(saved);

        String json = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john@test.com",
                  "age": 25,
                  "department": { "id": 1 }
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Department dept = new Department();
        dept.setId(1L);

        Employee existing = new Employee();
        existing.setId(1L);

        when(employeeService.getEmployeeById(1L))
                .thenReturn(Optional.of(existing));

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(dept));

        when(employeeService.saveEmployee(any(Employee.class)))
                .thenReturn(existing);

        String json = """
                {
                  "firstName": "Updated",
                  "lastName": "User",
                  "email": "u@test.com",
                  "age": 30,
                  "department": { "id": 1 }
                }
                """;

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);

        when(employeeService.getEmployeeById(1L))
                .thenReturn(Optional.of(emp));

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
}