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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerDeleteSweetTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should delete existing sweet successfully")
    void shouldDeleteExistingSweetSuccessfully() throws Exception {
        Long sweetId = 1L;
        Sweet existingSweet = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100,
                "http://example.com/laddu.jpg");
        existingSweet.setId(sweetId);

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.of(existingSweet));

        mockMvc.perform(delete("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sweetService).deleteSweet(sweetId);
    }

    @Test
    @DisplayName("should return 404 when trying to delete non-existent sweet")
    void shouldReturn404WhenTryingToDeleteNonExistentSweet() throws Exception {
        Long sweetId = 999L;

        when(sweetService.getSweetById(sweetId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/sweets/{id}", sweetId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
