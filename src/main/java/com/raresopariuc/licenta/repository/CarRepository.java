package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findById(Long carId);

    Page<Car> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    Optional<Car> findCarByPictureFiles_IdEquals(String pictureFileId);

    @Query("select car from Car car where (car.cubicCapacity >= ?1 or ?1 is null)" +
            "and (car.cubicCapacity <= ?2 or ?2 is null)" +
            "and (car.power >= ?3 or ?3 is null)" +
            "and (car.power <= ?4 or ?4 is null)" +
            "and (car.mileage >= ?5 or ?5 is null)" +
            "and (car.mileage <= ?6 or ?6 is null)" +
            "and (car.yearOfManufacture >= ?7 or ?7 is null)" +
            "and (car.yearOfManufacture <= ?8 or ?8 is null)" +
            "and (car.price >= ?9 or ?9 is null)" +
            "and (car.price <= ?10 or ?10 is null)")
    Page<Car> findCarsByFilter(Integer cubicCapacityMin,
                                   Integer cubicCapacityMax,
                                   Integer powerMin,
                                   Integer powerMax,
                                   Integer mileageMin,
                                   Integer mileageMax,
                                   Integer yearOfManufactureMin,
                                   Integer yearOfManufactureMax,
                                   Integer priceMin,
                                   Integer priceMax,
                                   Pageable pageable);
}
