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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SweetsController.class)
public class SweetsControllerPurchaseSweetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should purchase sweet successfully when sufficient stock available")
    void shouldPurchaseSweetSuccessfullyWhenSufficientStockAvailable() throws Exception {
        Long sweetId = 1L;
        Integer purchaseQuantity = 5;
        Sweet sweet = new Sweet("Gulab Jamun", "Traditional Indian sweet", 25.50, 40, 100,
                "http://example.com/gulab.jpg");
        sweet.setId(sweetId);

        Sweet updatedSweet = new Sweet("Gulab Jamun", "Traditional Indian sweet", 25.50, 40, 95,
                "http://example.com/gulab.jpg");
        updatedSweet.setId(sweetId);

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.of(sweet));
        when(sweetService.purchaseSweet(sweetId, purchaseQuantity)).thenReturn(updatedSweet);

        mockMvc.perform(post("/api/sweets/{id}/purchase", sweetId)
                .param("quantity", purchaseQuantity.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Sweet purchased successfully. Remaining stock: 95"));
    }

    @Test
    @DisplayName("should return 404 when trying to purchase non-existent sweet")
    void shouldReturn404WhenTryingToPurchaseNonExistentSweet() throws Exception {
        Long sweetId = 99L;
        Integer purchaseQuantity = 5;

        when(sweetService.purchaseSweet(sweetId, purchaseQuantity))
                .thenThrow(new IllegalArgumentException("Sweet not found with id: " + sweetId));

        mockMvc.perform(post("/api/sweets/{id}/purchase", sweetId)
                .param("quantity", purchaseQuantity.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when trying to purchase more than available stock")
    void shouldReturn400WhenTryingToPurchaseMoreThanAvailableStock() throws Exception {
        Long sweetId = 1L;
        Integer purchaseQuantity = 150; // More than available stock
        Sweet sweet = new Sweet("Gulab Jamun", "Traditional Indian sweet", 25.50, 40, 100,
                "http://example.com/gulab.jpg");
        sweet.setId(sweetId);

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.of(sweet));
        when(sweetService.purchaseSweet(sweetId, purchaseQuantity))
                .thenThrow(new IllegalArgumentException("Insufficient stock. Available: 100, Requested: 150"));

        mockMvc.perform(post("/api/sweets/{id}/purchase", sweetId)
                .param("quantity", purchaseQuantity.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient stock. Available: 100, Requested: 150"));
    }

    @Test
    @DisplayName("should return 400 when quantity is zero or negative")
    void shouldReturn400WhenQuantityIsZeroOrNegative() throws Exception {
        Long sweetId = 1L;

        // Test zero quantity
        mockMvc.perform(post("/api/sweets/{id}/purchase", sweetId)
                .param("quantity", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be a positive number"));

        // Test negative quantity
        mockMvc.perform(post("/api/sweets/{id}/purchase", sweetId)
                .param("quantity", "-5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be a positive number"));
    }
}
