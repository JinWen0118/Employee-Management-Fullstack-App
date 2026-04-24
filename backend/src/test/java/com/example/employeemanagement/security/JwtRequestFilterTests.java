package com.example.employeemanagement.security;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = new User(
                "testuser",
                "password",
                Collections.emptyList()
        );
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    void testDoFilterInternal_InvalidTokenException() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtTokenUtil.extractUsername("invalidToken"))
                .thenThrow(new RuntimeException("Invalid token"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtTokenUtil).extractUsername("invalidToken");
    }

    @Test
    void testDoFilterInternal_ValidTokenSetsAuthentication() throws Exception {
        String token = "validToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.extractUsername(token)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(token, "testuser")).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtTokenUtil).validateToken(token, "testuser");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidTokenDoesNotAuthenticate() throws Exception {
        String token = "invalidButParsable";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenUtil.extractUsername(token)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(token, "testuser")).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtTokenUtil).validateToken(token, "testuser");
        verify(filterChain).doFilter(request, response);
    }
}