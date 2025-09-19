package com.incubyte.sweetshopsystem.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sweets")
public class SweetsController {

    @PostMapping
    public ResponseEntity<String> createSweet(@RequestBody(required = false) String sweetJson) {
        if (sweetJson == null || sweetJson.trim().isEmpty()) {
            return new ResponseEntity<>("Request body cannot be empty", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sweet created successfully", HttpStatus.CREATED);
    }
}
