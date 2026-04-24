package com.example.employeemanagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    // ---------------- TEST CONTROLLER ----------------
    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/notfound")
        public String notFound() {
            throw new ResourceNotFoundException("Employee not found");
        }

        @GetMapping("/access")
        public String accessDenied() {
            throw new org.springframework.security.access.AccessDeniedException("Denied");
        }

        @GetMapping("/auth")
        public String auth() {
            throw new org.springframework.security.core.AuthenticationException("Auth failed") {};
        }

        @GetMapping("/generic")
        public String generic() {
            throw new RuntimeException("Unexpected error");
        }

        @PostMapping("/validation")
        public String validation(@Valid @RequestBody TestDTO dto) {
            return "ok";
        }

        @PostMapping("/badjson")
        public String badJson(@RequestBody TestDTO dto) {
            return "ok";
        }
    }

    // ---------------- DTO ----------------
    static class TestDTO {
        @NotBlank
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // ---------------- TEST CASES ----------------

    @Test
    void testResourceNotFound() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    void testAccessDenied() throws Exception {
        mockMvc.perform(get("/test/access"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void testAuthentication() throws Exception {
        mockMvc.perform(get("/test/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void testGenericException() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    void testMalformedJson() throws Exception {
        mockMvc.perform(post("/test/badjson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void testValidationException() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}