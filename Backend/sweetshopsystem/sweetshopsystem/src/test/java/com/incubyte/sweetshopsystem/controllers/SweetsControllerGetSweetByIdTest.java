package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.dto.SweetResponseDTO;
import com.incubyte.sweetshopsystem.service.SweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerGetSweetByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should return a sweet by ID when it exists")
    void shouldReturnSweetByIdWhenItExists() throws Exception {
        Long sweetId = 1L;
        // Create a SweetResponseDTO matching what the controller returns
        SweetResponseDTO sweet = new SweetResponseDTO(
            sweetId,
            "Laddu",
            "Traditional Indian sweet",
            BigDecimal.valueOf(150.0),
            1,
            "Category Name",
            100,
            "http://example.com/laddu.jpg",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        // Mock the method that the controller actually calls
        when(sweetService.getSweetByIdWithCategoryName(sweetId)).thenReturn(Optional.of(sweet));

        mockMvc.perform(get("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laddu"))
                .andExpect(jsonPath("$.description").value("Traditional Indian sweet"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.category_id").value(1))
                .andExpect(jsonPath("$.stock_quantity").value(100))
                .andExpect(jsonPath("$.image_url").value("http://example.com/laddu.jpg"));
    }

    @Test
    @DisplayName("should return 404 Not Found when sweet ID does not exist")
    void shouldReturnNotFoundWhenSweetIdDoesNotExist() throws Exception {
        Long sweetId = 99L;
        // Mock the method that the controller actually calls
        when(sweetService.getSweetByIdWithCategoryName(sweetId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
