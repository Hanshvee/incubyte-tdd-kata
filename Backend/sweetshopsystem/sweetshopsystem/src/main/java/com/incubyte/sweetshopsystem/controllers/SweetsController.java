package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO;
import com.incubyte.sweetshopsystem.dto.ImageUrlValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sweets")
@Validated
public class SweetsController {

    private final SweetService sweetService;

    public SweetsController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @PostMapping
    public ResponseEntity<String> createSweet(
            @Validated({ Default.class, ImageUrlValidationGroup.class }) @RequestBody SweetRequestDTO sweetRequestDTO) {
        // Map DTO to Sweet entity
        Sweet sweet = new Sweet(sweetRequestDTO.getName(),
                sweetRequestDTO.getDescription(),
                sweetRequestDTO.getPrice() != null ? sweetRequestDTO.getPrice().doubleValue() : 0.0, // Handle null
                                                                                                     // price
                sweetRequestDTO.getCategory_id(),
                sweetRequestDTO.getStock_quantity(),
                sweetRequestDTO.getImage_url());

        sweetService.save(sweet);

        return new ResponseEntity<>("Sweet created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Sweet>> getAllSweets() {
        List<Sweet> sweets = sweetService.getAllSweets();
        return new ResponseEntity<>(sweets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sweet> getSweetById(@PathVariable Long id) {
        return sweetService.getSweetById(id)
                .map(sweet -> new ResponseEntity<>(sweet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(
            @PathVariable Long id,
            @Validated({ Default.class, ImageUrlValidationGroup.class }) @RequestBody SweetRequestDTO sweetRequestDTO) {
        return sweetService.getSweetById(id)
                .map(existingSweet -> {
                    existingSweet.setName(sweetRequestDTO.getName());
                    existingSweet.setDescription(sweetRequestDTO.getDescription());
                    existingSweet.setPrice(sweetRequestDTO.getPrice() != null ? sweetRequestDTO.getPrice()
                            : existingSweet.getPrice());
                    existingSweet.setCategory_id(sweetRequestDTO.getCategory_id());
                    existingSweet.setStock_quantity(sweetRequestDTO.getStock_quantity());
                    existingSweet.setImage_url(sweetRequestDTO.getImage_url());
                    Sweet updatedSweet = sweetService.save(existingSweet);
                    return new ResponseEntity<>(updatedSweet, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<Sweet> sweets = sweetService.searchSweets(name, category, minPrice, maxPrice);
        return new ResponseEntity<>(sweets, HttpStatus.OK);
    }

    // Removed all manual validation methods: isValidImageUrl, isValidStockQuantity,
    // isValidCategoryId, isValidPrice
}

@ControllerAdvice
class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new AbstractMap.SimpleEntry<>(fieldName, errorMessage);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
