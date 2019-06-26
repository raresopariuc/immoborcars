package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findById(Long houseId);

    Page<House> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    Optional<House> findHouseByPictureFiles_IdEquals(String pictureFileId);

    @Query("select house from House house where (house.internalSurface >= ?1 or ?1 is null)" +
            "and (house.internalSurface <= ?2 or ?2 is null)" +
            "and (house.gardenSurface >= ?3 or ?3 is null)" +
            "and (house.gardenSurface <= ?4 or ?4 is null)" +
            "and (house.numberOfRooms >= ?5 or ?5 is null)" +
            "and (house.numberOfRooms <= ?6 or ?6 is null)" +
            "and (house.numberOfBathrooms >= ?7 or ?7 is null)" +
            "and (house.numberOfBathrooms <= ?8 or ?8 is null)" +
            "and (house.yearOfConstruction >= ?9 or ?9 is null)" +
            "and (house.yearOfConstruction <= ?10 or ?10 is null)" +
            "and (house.price >= ?11 or ?11 is null)" +
            "and (house.price <= ?12 or ?12 is null)")
    Page<House> findHousesByFilter(Integer internalSurfaceMin,
                                   Integer internalSurfaceMax,
                                   Integer gardenSurfaceMin,
                                   Integer gardenSurfaceMax,
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
