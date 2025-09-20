package com.incubyte.sweetshopsystem.repository;

import com.incubyte.sweetshopsystem.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SweetRepository extends JpaRepository<Sweet, Long> {

        @Query("SELECT s FROM Sweet s WHERE " +
                        "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:category IS NULL OR LOWER(s.description) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
                        "(:minPrice IS NULL OR s.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR s.price <= :maxPrice)")
        List<Sweet> findSweetsByCriteria(
                        @Param("name") String name,
                        @Param("category") String category,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice);
}
