package com.incubyte.sweetshopsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO;
import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerUpdateSweetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should update a sweet and return 200 OK when sweet exists")
    void shouldUpdateSweetAndReturnOkWhenSweetExists() throws Exception {
        Long sweetId = 1L;
        SweetRequestDTO updatedSweetDTO = new SweetRequestDTO("Updated Laddu", "Updated description",
                BigDecimal.valueOf(180.0), 2, 120, "http://example.com/updated_laddu.jpg");
        Sweet existingSweet = new Sweet("Laddu", "Original description", 150.0, 1, 100, "http://example.com/laddu.jpg");
        Sweet savedSweet = new Sweet("Updated Laddu", "Updated description", 180.0, 2, 120,
                "http://example.com/updated_laddu.jpg");
        savedSweet.setId(sweetId);

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.of(existingSweet));
        when(sweetService.save(any(Sweet.class))).thenReturn(savedSweet);

        mockMvc.perform(put("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSweetDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sweetId))
                .andExpect(jsonPath("$.name").value("Updated Laddu"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(180.0))
                .andExpect(jsonPath("$.category_id").value(2))
                .andExpect(jsonPath("$.stock_quantity").value(120))
                .andExpect(jsonPath("$.image_url").value("http://example.com/updated_laddu.jpg"));
    }

    @Test
    @DisplayName("should return 404 Not Found when attempting to update a non-existent sweet")
    void shouldReturnNotFoundWhenUpdatingNonExistentSweet() throws Exception {
        Long sweetId = 99L;
        SweetRequestDTO updatedSweetDTO = new SweetRequestDTO("Non-Existent Sweet", "Description",
                BigDecimal.valueOf(100.0), 1, 50, "http://example.com/non_existent.jpg");

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSweetDTO)))
                .andExpect(status().isNotFound());
    }
}
