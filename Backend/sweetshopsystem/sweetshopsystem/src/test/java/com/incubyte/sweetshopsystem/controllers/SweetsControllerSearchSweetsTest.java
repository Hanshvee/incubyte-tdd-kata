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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetsController.class)
public class SweetsControllerSearchSweetsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    @DisplayName("should return sweets by name search term")
    void shouldReturnSweetsByNameSearchTerm() throws Exception {
        String searchTerm = "Laddu";
        Sweet sweet = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100, "http://example.com/laddu.jpg");
        List<Sweet> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweets(anyString(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                        .param("name", searchTerm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laddu"));
    }
}
