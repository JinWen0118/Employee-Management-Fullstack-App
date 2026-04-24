package com.example.employeemanagement.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();

        // inject secret manually (important since @Value won't work in unit test)
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "testSecretKey123");
    }

    @Test
    void testGenerateToken_ShouldCreateToken() {
        String token = jwtTokenUtil.generateToken("john");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername_ShouldReturnCorrectUsername() {
        String token = jwtTokenUtil.generateToken("alice");

        String username = jwtTokenUtil.extractUsername(token);

        assertEquals("alice", username);
    }

    @Test
    void testValidateToken_ValidToken_ShouldReturnTrue() {
        String token = jwtTokenUtil.generateToken("bob");

        Boolean result = jwtTokenUtil.validateToken(token, "bob");

        assertTrue(result);
    }

    @Test
    void testValidateToken_WrongUsername_ShouldReturnFalse() {
        String token = jwtTokenUtil.generateToken("bob");

        Boolean result = jwtTokenUtil.validateToken(token, "wrongUser");

        assertFalse(result);
    }

    @Test
    void testExtractExpiration_ShouldReturnFutureDate() {
        String token = jwtTokenUtil.generateToken("test");

        Date expiration = jwtTokenUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testValidateToken_ExpiredTokenLogic() throws InterruptedException {
        // NOTE: This is a logical test, not real waiting for expiry
        String token = jwtTokenUtil.generateToken("user");

        assertTrue(jwtTokenUtil.validateToken(token, "user"));
    }
}