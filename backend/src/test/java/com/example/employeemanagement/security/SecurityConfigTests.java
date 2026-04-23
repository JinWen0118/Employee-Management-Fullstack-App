package com.example.employeemanagement.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SecurityConfig.class)
class SecurityConfigTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void testPasswordEncoderBeanExists() {
        assertNotNull(passwordEncoder);

        String encoded = passwordEncoder.encode("test123");
        assertTrue(passwordEncoder.matches("test123", encoded));
    }

    @Test
    void testAuthenticationManagerBeanExists() {
        assertNotNull(authenticationManager);
    }
}