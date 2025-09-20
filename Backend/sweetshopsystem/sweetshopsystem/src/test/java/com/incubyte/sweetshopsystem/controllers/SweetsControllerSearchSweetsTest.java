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

    @Test
    @DisplayName("should return sweets by category search term")
    void shouldReturnSweetsByCategorySearchTerm() throws Exception {
        String categoryTerm = "Traditional";
        Sweet sweet1 = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100, "http://example.com/laddu.jpg");
        Sweet sweet2 = new Sweet("Gulab Jamun", "Traditional dessert", 200.0, 1, 50, "http://example.com/gulab.jpg");
        List<Sweet> sweets = List.of(sweet1, sweet2);

        when(sweetService.searchSweets(any(), anyString(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .param("category", categoryTerm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laddu"))
                .andExpect(jsonPath("$[1].name").value("Gulab Jamun"));
    }

    @Test
    @DisplayName("should return sweets by price range")
    void shouldReturnSweetsByPriceRange() throws Exception {
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = new BigDecimal("200.00");
        Sweet sweet = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100, "http://example.com/laddu.jpg");
        List<Sweet> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweets(any(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .param("minPrice", minPrice.toString())
                .param("maxPrice", maxPrice.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laddu"));
    }

    @Test
    @DisplayName("should return sweets by combined search criteria")
    void shouldReturnSweetsByCombinedSearchCriteria() throws Exception {
        String name = "Laddu";
        String category = "Traditional";
        BigDecimal minPrice = new BigDecimal("100.00");
        Sweet sweet = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100, "http://example.com/laddu.jpg");
        List<Sweet> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweets(anyString(), anyString(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .param("name", name)
                .param("category", category)
                .param("minPrice", minPrice.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laddu"));
    }

    @Test
    @DisplayName("should return empty list when no sweets match search criteria")
    void shouldReturnEmptyListWhenNoSweetsMatchSearchCriteria() throws Exception {
        String searchTerm = "NonExistentSweet";
        List<Sweet> sweets = Collections.emptyList();

        when(sweetService.searchSweets(anyString(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .param("name", searchTerm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("should return all sweets when no search parameters provided")
    void shouldReturnAllSweetsWhenNoSearchParametersProvided() throws Exception {
        Sweet sweet1 = new Sweet("Laddu", "Traditional Indian sweet", 150.0, 1, 100, "http://example.com/laddu.jpg");
        Sweet sweet2 = new Sweet("Gulab Jamun", "Traditional dessert", 200.0, 1, 50, "http://example.com/gulab.jpg");
        List<Sweet> sweets = List.of(sweet1, sweet2);

        when(sweetService.searchSweets(any(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laddu"))
                .andExpect(jsonPath("$[1].name").value("Gulab Jamun"));
    }
}
