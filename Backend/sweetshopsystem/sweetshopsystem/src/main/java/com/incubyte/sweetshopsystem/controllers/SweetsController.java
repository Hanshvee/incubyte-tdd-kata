package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO; // Added import
import com.incubyte.sweetshopsystem.dto.ImageUrlValidationGroup; // Added import for validation groups
import jakarta.validation.Valid; // Added import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated; // Added import for @Validated on class level
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap; // Added import for AbstractMap.SimpleEntry
import java.util.stream.Collectors;

// Removed unused imports: import org.json.JSONObject; import org.json.JSONException;

@RestController
@RequestMapping("/api/sweets")
@Validated // Added @Validated for group validation
public class SweetsController {

    private final SweetService sweetService;

    public SweetsController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @PostMapping
    public ResponseEntity<String> createSweet(
            @Validated(ImageUrlValidationGroup.class) @RequestBody SweetRequestDTO sweetRequestDTO) {
        // Map DTO to Sweet entity
        Sweet sweet = new Sweet(sweetRequestDTO.getName(),
                sweetRequestDTO.getDescription(),
                sweetRequestDTO.getPrice().doubleValue(), // Convert BigDecimal to double
                sweetRequestDTO.getCategory_id(),
                sweetRequestDTO.getStock_quantity(),
                sweetRequestDTO.getImage_url());

        sweetService.save(sweet);

        return new ResponseEntity<>("Sweet created successfully", HttpStatus.CREATED);
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
