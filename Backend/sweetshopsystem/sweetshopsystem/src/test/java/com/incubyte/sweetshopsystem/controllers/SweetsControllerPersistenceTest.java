package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when; // Added import
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerPersistenceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should save a valid sweet via POST /api/sweets")
    void shouldSaveValidSweet() throws Exception {
        JSONObject sweetJson = new JSONObject();
        sweetJson.put("name", "Gulab Jamun");
        sweetJson.put("description", "A classic Indian sweet");
        sweetJson.put("price", 50.00);
        sweetJson.put("category_id", 1);
        sweetJson.put("stock_quantity", 100);
        sweetJson.put("image_url", "http://example.com/gulabjamun.jpg");

        // Mock the service to return a saved sweet with an ID
        Sweet savedSweet = new Sweet("Gulab Jamun", "A classic Indian sweet", 50.00, 1, 100,
                "http://example.com/gulabjamun.jpg");
        savedSweet.setId(1L);
        when(sweetService.save(any(Sweet.class))).thenReturn(savedSweet); // Mock the save method

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sweetJson.toString()))
                .andExpect(status().isCreated());

        verify(sweetService, times(1)).save(any(Sweet.class));
    }
}
