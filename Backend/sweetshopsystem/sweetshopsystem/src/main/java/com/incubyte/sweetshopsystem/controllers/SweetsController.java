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
        } catch (JSONException e) {
            return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Sweet created successfully", HttpStatus.CREATED);
    }
}
