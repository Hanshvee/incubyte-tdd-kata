package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.dto.SweetResponseDTO;
import com.incubyte.sweetshopsystem.service.SweetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        SweetResponseDTO sweet = new SweetResponseDTO(1L, "Laddu", "Traditional Indian sweet", 
                new BigDecimal(150.0), 1, "Traditional", 100, 
                "http://example.com/laddu.jpg", LocalDateTime.now(), LocalDateTime.now());
        List<SweetResponseDTO> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweetsWithCategoryNames(anyString(), any(), any(), any())).thenReturn(sweets);

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
        SweetResponseDTO sweet1 = new SweetResponseDTO(1L, "Laddu", "Traditional Indian sweet", 
                new BigDecimal(150.0), 1, "Traditional", 100, 
                "http://example.com/laddu.jpg", LocalDateTime.now(), LocalDateTime.now());
        SweetResponseDTO sweet2 = new SweetResponseDTO(2L, "Gulab Jamun", "Traditional dessert", 
                new BigDecimal(200.0), 1, "Traditional", 50, 
                "http://example.com/gulab.jpg", LocalDateTime.now(), LocalDateTime.now());
        List<SweetResponseDTO> sweets = List.of(sweet1, sweet2);

        when(sweetService.searchSweetsWithCategoryNames(any(), anyString(), any(), any())).thenReturn(sweets);

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
        SweetResponseDTO sweet = new SweetResponseDTO(1L, "Laddu", "Traditional Indian sweet", 
                new BigDecimal(150.0), 1, "Traditional", 100, 
                "http://example.com/laddu.jpg", LocalDateTime.now(), LocalDateTime.now());
        List<SweetResponseDTO> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweetsWithCategoryNames(any(), any(), any(), any())).thenReturn(sweets);

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
        SweetResponseDTO sweet = new SweetResponseDTO(1L, "Laddu", "Traditional Indian sweet", 
                new BigDecimal(150.0), 1, "Traditional", 100, 
                "http://example.com/laddu.jpg", LocalDateTime.now(), LocalDateTime.now());
        List<SweetResponseDTO> sweets = Collections.singletonList(sweet);

        when(sweetService.searchSweetsWithCategoryNames(anyString(), anyString(), any(), any())).thenReturn(sweets);

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
        List<SweetResponseDTO> sweets = Collections.emptyList();

        when(sweetService.searchSweetsWithCategoryNames(anyString(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .param("name", searchTerm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("should return all sweets when no search parameters provided")
    void shouldReturnAllSweetsWhenNoSearchParametersProvided() throws Exception {
        SweetResponseDTO sweet1 = new SweetResponseDTO(1L, "Laddu", "Traditional Indian sweet", 
                new BigDecimal(150.0), 1, "Traditional", 100, 
                "http://example.com/laddu.jpg", LocalDateTime.now(), LocalDateTime.now());
        SweetResponseDTO sweet2 = new SweetResponseDTO(2L, "Gulab Jamun", "Traditional dessert", 
                new BigDecimal(200.0), 1, "Traditional", 50, 
                "http://example.com/gulab.jpg", LocalDateTime.now(), LocalDateTime.now());
        List<SweetResponseDTO> sweets = List.of(sweet1, sweet2);

        when(sweetService.searchSweetsWithCategoryNames(any(), any(), any(), any())).thenReturn(sweets);

        mockMvc.perform(get("/api/sweets/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laddu"))
                .andExpect(jsonPath("$[1].name").value("Gulab Jamun"));
    }

    // Added missing import
    @Test
    @DisplayName("should handle invalid input gracefully")
    void shouldHandleInvalidInputGracefully() throws Exception {
        // Test with invalid price format
        mockMvc.perform(get("/api/sweets/search")
                .param("minPrice", "invalid-price")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
