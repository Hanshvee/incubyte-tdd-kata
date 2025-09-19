package com.incubyte.sweetshopsystem.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SweetsController.class)
class SweetsControllerCreateSweetSuccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return created status and success message for a valid sweet creation")
    void shouldReturnCreatedStatusAndSuccessMessageForValidSweetCreation() throws Exception {
        String sweetJson = "{\"name\": \"Chocolate Delight\", \"description\": \"A rich and creamy chocolate sweet\", \"price\": 15.75, \"category_id\": 1, \"stock_quantity\": 200, \"image_url\": \"https://example.com/chocolatedelight.jpg\"}";

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sweetJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Sweet created successfully"));
    }
}
