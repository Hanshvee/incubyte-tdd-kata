package com.incubyte.sweetshopsystem.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
class SweetsControllerInvalidImageUrlFormatTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return bad request when image_url field has an invalid format")
    void shouldReturnBadRequestWhenImageUrlHasInvalidFormat() throws Exception {
        String sweetJson = "{\"name\": \"Chocolate Bar\", \"description\": \"A delicious chocolate sweet\", \"price\": 10.50, \"category_id\": 1, \"stock_quantity\": 100, \"image_url\": \"invalid-url\"}";

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sweetJson))
                .andExpect(status().isBadRequest());
    }
}
