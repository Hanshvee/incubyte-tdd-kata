package com.incubyte.sweetshopsystem.service;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.repository.SweetRepository;
import org.springframework.stereotype.Service;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    public Sweet save(Sweet sweet) {
        return sweetRepository.save(sweet);
    }

    public java.util.List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }
}
