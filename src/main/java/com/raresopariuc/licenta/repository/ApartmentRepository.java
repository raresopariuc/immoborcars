package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<House, Long> {
    Optional<House> findById(Long apartmentId);

    Page<House> findByCreatedBy(Long userId, Pageable pageable);
}
