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
class SweetsControllerMissingPriceFieldTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should return bad request when price field is missing")
    void shouldReturnBadRequestWhenPriceFieldIsMissing() throws Exception {
        String sweetJson = "{\"name\": \"Chocolate Bar\", \"description\": \"A delicious chocolate sweet\"}";

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sweetJson))
                .andExpect(status().isBadRequest());
    }
}
