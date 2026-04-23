package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.security.JwtTokenUtil;
import com.example.employeemanagement.security.JwtRequestFilter;
import com.example.employeemanagement.service.DepartmentService;
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
@WebMvcTest(controllers = DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DepartmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    // ✅ FULL SECURITY MOCKING (FIX)
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllDepartments() throws Exception {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        when(departmentService.getAllDepartments()).thenReturn(List.of(dept));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("IT"));
    }

    @Test
    void testGetDepartmentById_Found() throws Exception {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(dept));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void testGetDepartmentById_NotFound() throws Exception {
        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDepartment() throws Exception {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        when(departmentService.saveDepartment(any(Department.class)))
                .thenReturn(dept);

        String json = objectMapper.writeValueAsString(new Department() {{
            setName("IT");
        }});

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        Department existing = new Department();
        existing.setId(1L);
        existing.setName("Updated");

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(existing));

        when(departmentService.saveDepartment(any(Department.class)))
                .thenReturn(existing);

        String json = """
                {
                  "name": "Updated"
                }
                """;

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testDeleteDepartment_Success() throws Exception {
        Department dept = new Department();
        dept.setId(1L);

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(dept));

        when(departmentService.countEmployeesInDepartment(1L))
                .thenReturn(0L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteDepartment_WithEmployees() throws Exception {
        Department dept = new Department();
        dept.setId(1L);

        when(departmentService.getDepartmentById(1L))
                .thenReturn(Optional.of(dept));

        when(departmentService.countEmployeesInDepartment(1L))
                .thenReturn(3L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}