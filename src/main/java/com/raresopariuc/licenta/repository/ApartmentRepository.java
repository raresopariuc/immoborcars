package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    Optional<Apartment> findById(Long apartmentId);

    Page<Apartment> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    Optional<Apartment> findApartmentByPictureFiles_IdEquals(String pictureFileId);

    @Query("select apartment from Apartment apartment where (apartment.internalSurface >= ?1 or ?1 is null)" +
            "and (apartment.internalSurface <= ?2 or ?2 is null)" +
            "and (apartment.floorNumber >= ?3 or ?3 is null)" +
            "and (apartment.floorNumber <= ?4 or ?4 is null)" +
            "and (apartment.numberOfRooms >= ?5 or ?5 is null)" +
            "and (apartment.numberOfRooms <= ?6 or ?6 is null)" +
            "and (apartment.numberOfBathrooms >= ?7 or ?7 is null)" +
            "and (apartment.numberOfBathrooms <= ?8 or ?8 is null)" +
            "and (apartment.yearOfConstruction >= ?9 or ?9 is null)" +
            "and (apartment.yearOfConstruction <= ?10 or ?10 is null)" +
            "and (apartment.price >= ?11 or ?11 is null)" +
            "and (apartment.price <= ?12 or ?12 is null)")
    Page<Apartment> findApartmentsByFilter(Integer internalSurfaceMin,
                                   Integer internalSurfaceMax,
                                   Integer floorNumberMin,
                                   Integer floorNumberMax,
                                   Integer numberOfRoomsMin,
                                   Integer numberOfRoomsMax,
                                   Integer numberOfBathroomsMin,
                                   Integer numberOfBathroomsMax,
                                   Integer yearOfConstructionMin,
                                   Integer yearOfConstructionMax,
                                   Integer priceMin,
                                   Integer priceMax,
                                   Pageable pageable);
}
