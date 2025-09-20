package com.incubyte.sweetshopsystem.repository;

import com.incubyte.sweetshopsystem.entity.Sweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SweetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SweetRepository sweetRepository;

    private Sweet sweet;

    @BeforeEach
    public void setUp() {
        // This constructor call will fail compilation initially, which is expected for
        // the Red Phase
        sweet = new Sweet("Test Sweet", "Description for test sweet", 10.50, 1, 100, "http://example.com/test.jpg");
    }

    @Test
    public void whenSaveSweet_thenSweetIsPersisted() {
        Sweet savedSweet = sweetRepository.save(sweet);
        assertThat(savedSweet).isNotNull();
        assertThat(savedSweet.getId()).isNotNull();
        Sweet foundSweet = entityManager.find(Sweet.class, savedSweet.getId());
        assertThat(foundSweet).isEqualTo(savedSweet);
    }
}
