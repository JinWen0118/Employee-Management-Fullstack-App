package com.example.employeemanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldSetAndGetId() {
        User user = new User();
        user.setId(1L);

        assertEquals(1L, user.getId());
    }

    @Test
    void shouldSetAndGetUsername() {
        User user = new User();
        user.setUsername("john_doe");

        assertEquals("john_doe", user.getUsername());
    }

    @Test
    void shouldSetAndGetPassword() {
        User user = new User();
        user.setPassword("secure123");

        assertEquals("secure123", user.getPassword());
    }

    @Test
    void shouldHandleFullUserObject() {
        User user = new User();

        user.setId(10L);
        user.setUsername("alice");
        user.setPassword("password123");

        assertAll(
            () -> assertEquals(10L, user.getId()),
            () -> assertEquals("alice", user.getUsername()),
            () -> assertEquals("password123", user.getPassword())
        );
    }

    @Test
    void shouldAllowNullValues() {
        User user = new User();

        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }
}