package com.incubyte.sweetshopsystem.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.json.JSONException;

@RestController
@RequestMapping("/api/sweets")
public class SweetsController {

    @PostMapping
    public ResponseEntity<String> createSweet(@RequestBody(required = false) String sweetJson) {
        if (sweetJson == null || sweetJson.trim().isEmpty()) {
            return new ResponseEntity<>("Request body cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            JSONObject jsonObject = new JSONObject(sweetJson);
            if (!jsonObject.has("name") || jsonObject.getString("name").trim().isEmpty()) {
                return new ResponseEntity<>("Sweet name is required", HttpStatus.BAD_REQUEST);
            }

            if (!jsonObject.has("price") || !isValidPrice(jsonObject.get("price"))) {
                return new ResponseEntity<>("Sweet price is required and must be a valid number",
                        HttpStatus.BAD_REQUEST);
            }

            if (!jsonObject.has("description") || jsonObject.getString("description").trim().isEmpty()) {
                return new ResponseEntity<>("Sweet description is required", HttpStatus.BAD_REQUEST);
            }

            if (!jsonObject.has("category_id") || !isValidCategoryId(jsonObject.get("category_id"))) {
                return new ResponseEntity<>("Sweet category ID is required and must be a valid integer",
                        HttpStatus.BAD_REQUEST);
            }

            if (!jsonObject.has("stock_quantity") || !isValidStockQuantity(jsonObject.get("stock_quantity"))) {
                return new ResponseEntity<>("Sweet stock quantity is required and must be a non-negative integer",
                        HttpStatus.BAD_REQUEST);
            }

            if (jsonObject.has("image_url") && jsonObject.getString("image_url").trim().isEmpty()) {
                return new ResponseEntity<>("Sweet image URL cannot be empty if provided", HttpStatus.BAD_REQUEST);
            }
        } catch (JSONException e) {
            return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Sweet created successfully", HttpStatus.CREATED);
    }

    private boolean isValidStockQuantity(Object stockQuantity) {
        if (stockQuantity == null) {
            return false;
        }
        if (stockQuantity instanceof Number) {
            return ((Number) stockQuantity).intValue() >= 0; // Stock quantity can be 0 or positive
        }
        if (stockQuantity instanceof String) {
            try {
                return Integer.parseInt((String) stockQuantity) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isValidCategoryId(Object categoryId) {
        if (categoryId == null) {
            return false;
        }
        if (categoryId instanceof Number) {
            return ((Number) categoryId).intValue() > 0; // Category IDs typically start from 1
        }
        if (categoryId instanceof String) {
            try {
                return Integer.parseInt((String) categoryId) > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isValidPrice(Object price) {
        if (price == null) {
            return false;
        }
        if (price instanceof Number) {
            return ((Number) price).doubleValue() > 0;
        }
        if (price instanceof String) {
            try {
                return Double.parseDouble((String) price) > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
