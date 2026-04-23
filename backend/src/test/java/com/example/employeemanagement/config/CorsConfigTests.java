package com.example.employeemanagement.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(CorsConfig.class);

    @Test
    void testCorsConfigurerBeanExists() {
        contextRunner.run(context -> {
            assertTrue(context.containsBean("corsConfigurer"));
        });
    }
}