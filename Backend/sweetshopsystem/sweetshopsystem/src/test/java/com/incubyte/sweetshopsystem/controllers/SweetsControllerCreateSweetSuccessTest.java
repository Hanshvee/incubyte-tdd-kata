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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SweetsController.class)
class SweetsControllerCreateSweetSuccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should return created status and success message for a valid sweet creation")
    void shouldReturnCreatedStatusAndSuccessMessageForValidSweetCreation() throws Exception {
        SweetRequestDTO sweetRequestDTO = new SweetRequestDTO(
                "Chocolate Delight",
                "A rich and creamy chocolate sweet",
                BigDecimal.valueOf(15.75),
                1,
                200,
                "https://example.com/chocolatedelight.jpg");

        when(sweetService.save(any(Sweet.class))).thenReturn(new Sweet("Chocolate Delight",
                "A rich and creamy chocolate sweet", 15.75, 1, 200, "https://example.com/chocolatedelight.jpg"));

        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject(sweetRequestDTO).toString()))
                .andExpect(status().isCreated())
                .andExpect(content().string("Sweet created successfully"));
    }
}
