package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testRegisterUser_Success() throws Exception {

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(new User());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"1234\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {

        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);

        Mockito.when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(mockUserDetails);

        Mockito.when(jwtTokenUtil.generateToken(anyString()))
                .thenReturn("fake-jwt-token");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(null);

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"password\":\"1234\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testVerifyUsername_Found() throws Exception {

        User user = new User();
        user.setUsername("john");

        Mockito.when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/verify-username/john"))
                .andExpect(status().isOk());
    }

    @Test
    void testVerifyUsername_NotFound() throws Exception {

        Mockito.when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/verify-username/john"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testResetPassword_Success() throws Exception {

        User user = new User();
        user.setUsername("john");

        Mockito.when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"newPassword\":\"newpass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPassword_NotFound() throws Exception {

        Mockito.when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"newPassword\":\"newpass\"}"))
                .andExpect(status().isNotFound());
    }
}