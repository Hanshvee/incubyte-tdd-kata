package com.incubyte.sweetshopsystem.service;

import com.incubyte.sweetshopsystem.entity.Sweet;
import com.incubyte.sweetshopsystem.repository.SweetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @Test
    @DisplayName("should save a sweet successfully")
    void shouldSaveSweetSuccessfully() {
        Sweet sweet = new Sweet("Gulab Jamun", "A classic Indian sweet", 50.00, 1, 100,
                "https://example.com/gulabjamun.jpg");

        sweetService.save(sweet);

        verify(sweetRepository).save(sweet);
    }
}
