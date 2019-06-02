package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findById(Long houseId);

    Page<House> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);
}
