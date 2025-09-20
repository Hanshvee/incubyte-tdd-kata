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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SweetsController.class)
public class SweetsControllerDescriptionValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return bad request when sweet description is blank")
    void shouldReturnBadRequestWhenSweetDescriptionIsBlank() throws Exception {
        SweetRequestDTO sweetRequestDTO = new SweetRequestDTO(
                "Gulab Jamun",
                "", // Blank description
                BigDecimal.valueOf(10.50),
                1,
                100,
                "http://example.com/gulabjamun.jpg");

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sweetRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Sweet description is required"));
    }
}
