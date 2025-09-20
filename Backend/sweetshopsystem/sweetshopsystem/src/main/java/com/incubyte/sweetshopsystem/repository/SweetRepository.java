package com.incubyte.sweetshopsystem.repository;

import com.incubyte.sweetshopsystem.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SweetRepository extends JpaRepository<Sweet, Long> {
}
