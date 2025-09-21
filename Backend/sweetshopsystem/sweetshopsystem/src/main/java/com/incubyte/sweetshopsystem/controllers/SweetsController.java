package com.incubyte.sweetshopsystem.controllers;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.service.SweetService;
import com.incubyte.sweetshopsystem.dto.SweetRequestDTO;
import com.incubyte.sweetshopsystem.dto.SweetResponseDTO;
import com.incubyte.sweetshopsystem.dto.ImageUrlValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public ResponseEntity<SweetResponseDTO> createSweet(
        @Validated({ Default.class, ImageUrlValidationGroup.class }) @RequestBody SweetRequestDTO sweetRequestDTO) {

    // Map DTO to Sweet entity
    Sweet sweet = new Sweet(
        sweetRequestDTO.getName(),
        sweetRequestDTO.getDescription(),
        sweetRequestDTO.getPrice() != null ? sweetRequestDTO.getPrice().doubleValue() : 0.0,
        sweetRequestDTO.getCategory_id(),
        sweetRequestDTO.getStock_quantity(),
        sweetRequestDTO.getImage_url()
    );

    // Save sweet
    Sweet savedSweet = sweetService.save(sweet);

    // Map saved entity to ResponseDTO
    SweetResponseDTO responseDTO = sweetService.getSweetByIdWithCategoryName(savedSweet.getId())
                                              .orElseThrow(() -> new RuntimeException("Sweet not found after save"));

    // Return created sweet as JSON
    return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
}


    @GetMapping
    public ResponseEntity<List<SweetResponseDTO>> getAllSweets() {
        List<SweetResponseDTO> sweets = sweetService.getAllSweetsWithCategoryNames();
        return new ResponseEntity<>(sweets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SweetResponseDTO> getSweetById(@PathVariable Long id) {
        return sweetService.getSweetByIdWithCategoryName(id)
                .map(sweet -> new ResponseEntity<>(sweet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SweetResponseDTO> updateSweet(
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
                    SweetResponseDTO responseDTO = sweetService.getSweetByIdWithCategoryName(updatedSweet.getId())
                            .orElse(null);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SweetResponseDTO>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<SweetResponseDTO> sweets = sweetService.searchSweetsWithCategoryNames(name, category, minPrice, maxPrice);
        return new ResponseEntity<>(sweets, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSweet(@PathVariable Long id) {
        return sweetService.getSweetById(id)
                .map(sweet -> {
                    sweetService.deleteSweet(id);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Sweet deleted successfully");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<String> purchaseSweet(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        // Input validation
        if (quantity == null || quantity <= 0) {
            return new ResponseEntity<>("Quantity must be a positive number", HttpStatus.BAD_REQUEST);
        }

        try {
            Sweet updatedSweet = sweetService.purchaseSweet(id, quantity);
            return new ResponseEntity<>(
                    "Sweet purchased successfully. Remaining stock: " + updatedSweet.getStock_quantity(),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Sweet not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restockSweet(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        // Input validation
        if (quantity == null || quantity <= 0) {
            return new ResponseEntity<>("Quantity must be a positive number", HttpStatus.BAD_REQUEST);
        }

        try {
            Sweet updatedSweet = sweetService.restockSweet(id, quantity);
            return new ResponseEntity<>(
                    "Sweet restocked successfully. New stock: " + updatedSweet.getStock_quantity(),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Sweet not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
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

    // Global handler for all other exceptions to ensure JSON response
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
