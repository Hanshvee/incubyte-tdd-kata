package com.incubyte.sweetshopsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO;
import com.incubyte.sweetshopsystem.service.SweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerImageUrlValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return bad request when sweet image URL has an invalid format")
    void shouldReturnBadRequestWhenSweetImageUrlIsInvalid() throws Exception {
        SweetRequestDTO sweetRequestDTO = new SweetRequestDTO(
                "Gulab Jamun",
                "A delicious sweet",
                BigDecimal.valueOf(10.50),
                1,
                100,
                "invalid-url" // Invalid image URL format
        );

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sweetRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.image_url").value("Sweet image URL has an invalid format"));
    }
}
