package com.example.employeemanagement.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTests {

    private final HomeController homeController = new HomeController();

    @Test
    void testRedirectToSwagger() {
        String result = homeController.redirectToSwagger();

        assertNotNull(result);
        assertEquals("redirect:/swagger-ui.html", result);
    }
}