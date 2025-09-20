package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerRestockSweetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should restock sweet successfully when valid sweet exists")
    void shouldRestockSweetSuccessfullyWhenValidSweetExists() throws Exception {
        Long sweetId = 1L;
        Integer restockQuantity = 50;
        Sweet sweet = new Sweet("Gulab Jamun", "Traditional Indian sweet", 25.50, 40, 100,
                "http://example.com/gulab.jpg");
        sweet.setId(sweetId);

        Sweet updatedSweet = new Sweet("Gulab Jamun", "Traditional Indian sweet", 25.50, 40, 150,
                "http://example.com/gulab.jpg");
        updatedSweet.setId(sweetId);

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.of(sweet));
        when(sweetService.restockSweet(sweetId, restockQuantity)).thenReturn(updatedSweet);

        mockMvc.perform(post("/api/sweets/{id}/restock", sweetId)
                .param("quantity", restockQuantity.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Sweet restocked successfully. New stock: 150"));
    }

    @Test
    @DisplayName("should return 404 when trying to restock non-existent sweet")
    void shouldReturn404WhenTryingToRestockNonExistentSweet() throws Exception {
        Long sweetId = 99L;
        Integer restockQuantity = 50;

        when(sweetService.restockSweet(sweetId, restockQuantity))
                .thenThrow(new IllegalArgumentException("Sweet not found with id: " + sweetId));

        mockMvc.perform(post("/api/sweets/{id}/restock", sweetId)
                .param("quantity", restockQuantity.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when trying to restock with zero or negative quantity")
    void shouldReturn400WhenTryingToRestockWithZeroOrNegativeQuantity() throws Exception {
        Long sweetId = 1L;

        // Test zero quantity
        mockMvc.perform(post("/api/sweets/{id}/restock", sweetId)
                .param("quantity", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be a positive number"));

        // Test negative quantity
        mockMvc.perform(post("/api/sweets/{id}/restock", sweetId)
                .param("quantity", "-10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be a positive number"));
    }
}
