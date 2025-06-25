package com.growtivat.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.NewUserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/auth";

    @Test
    void registerUserReturnsOK() throws Exception {
        NewUserRequestDto dto = NewUserRequestDto.builder()
                .username("johndoe")
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@email.com")
                .password("password123")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

//    @Test
//    void authenticateUserReturnsOK() throws Exception {
//        NewUserRequestDto dto = NewUserRequestDto.builder()
//                .username("johndoe")
//                .firstname("John")
//                .lastname("Doe")
//                .email("johndoe@email.com")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post(BASE_URL + "/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)));
//
//        AuthenticateUserRequestDto authUserDto = AuthenticateUserRequestDto.builder()
//                .username("johndoe")
//                .password("password123")
//                .build();
//
//        mockMvc.perform(post(BASE_URL + "/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authUserDto)))
//                .andExpect(status().isOk())
//                .andExpect(cookie().exists("createNewAccessTokenFrom"));
//
//    }
}
