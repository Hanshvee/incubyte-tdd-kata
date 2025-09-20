package com.incubyte.sweetshopsystem.service;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.entity.Category;
import com.incubyte.sweetshopsystem.repository.SweetRepository;
import com.incubyte.sweetshopsystem.repository.CategoryRepository;
import com.incubyte.sweetshopsystem.dto.SweetResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;
    private final CategoryRepository categoryRepository;

    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
    }

    public Sweet save(Sweet sweet) {
        return sweetRepository.save(sweet);
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    public Optional<Sweet> getSweetById(Long id) {
        return sweetRepository.findById(id);
    }

    public List<Sweet> searchSweets(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return sweetRepository.findSweetsByCriteria(name, category, minPrice, maxPrice);
    }

    public void deleteSweet(Long id) {
        sweetRepository.deleteById(id);
    }

    public Sweet purchaseSweet(Long id, Integer quantity) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sweet not found with id: " + id));

        if (sweet.getStock_quantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Available: " + sweet.getStock_quantity() + ", Requested: " + quantity);
        }

        sweet.setStock_quantity(sweet.getStock_quantity() - quantity);
        sweet.setUpdated_at(LocalDateTime.now());

        return sweetRepository.save(sweet);
    }

    // New methods for enhanced responses with category names
    public List<SweetResponseDTO> getAllSweetsWithCategoryNames() {
        List<Sweet> sweets = sweetRepository.findAll();
        return convertToResponseDTOs(sweets);
    }

    public Optional<SweetResponseDTO> getSweetByIdWithCategoryName(Long id) {
        return sweetRepository.findById(id)
                .map(sweet -> convertToResponseDTO(sweet));
    }

    public List<SweetResponseDTO> searchSweetsWithCategoryNames(String name, String category, BigDecimal minPrice,
            BigDecimal maxPrice) {
        List<Sweet> sweets = sweetRepository.findSweetsByCriteria(name, category, minPrice, maxPrice);
        return convertToResponseDTOs(sweets);
    }

    private List<SweetResponseDTO> convertToResponseDTOs(List<Sweet> sweets) {
        if (sweets.isEmpty()) {
            return Collections.emptyList();
        }

        // Get all category IDs from sweets
        List<Integer> categoryIds = sweets.stream()
                .map(Sweet::getCategory_id)
                .distinct()
                .collect(Collectors.toList());

        // Fetch all categories at once
        Map<Integer, String> categoryMap = categoryRepository.findAllById(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getCategory_id, Category::getName));

        // Convert sweets to response DTOs
        return sweets.stream()
                .map(sweet -> convertToResponseDTO(sweet, categoryMap))
                .collect(Collectors.toList());
    }

    private SweetResponseDTO convertToResponseDTO(Sweet sweet) {
        String categoryName = categoryRepository.findById(sweet.getCategory_id())
                .map(Category::getName)
                .orElse("Unknown Category");
        return convertToResponseDTO(sweet, Map.of(sweet.getCategory_id(), categoryName));
    }

    private SweetResponseDTO convertToResponseDTO(Sweet sweet, Map<Integer, String> categoryMap) {
        String categoryName = categoryMap.getOrDefault(sweet.getCategory_id(), "Unknown Category");

        return new SweetResponseDTO(
                sweet.getId(),
                sweet.getName(),
                sweet.getDescription(),
                sweet.getPrice(),
                sweet.getCategory_id(),
                categoryName,
                sweet.getStock_quantity(),
                sweet.getImage_url(),
                sweet.getCreated_at(),
                sweet.getUpdated_at());
    }
}
