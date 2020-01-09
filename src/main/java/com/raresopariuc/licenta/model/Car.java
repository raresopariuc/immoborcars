package com.raresopariuc.licenta.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car extends Announcement {
    @NotNull
    @Positive
    private Integer cubicCapacity;

    @NotNull
    @Positive
    private Integer power;

    @NotNull
    @Positive
    private Integer mileage;

    @NotNull
    @Positive
    private Integer yearOfManufacture;

    @NotBlank
    @Size(max = 40)
    private String fuel;

    @NotBlank
    @Size(max = 40)
    private String gearbox;

    @NotBlank
    @Size(max = 40)
    private String emissionClass;

    @Size(max = 40)
    private String vin;

    @Builder
    public Car(String title, String description, Integer price, Integer cubicCapacity, Integer power, Integer mileage,
               Integer yearOfManufacture, String fuel, String gearbox, String emissionClass, String vin, Double latitude, Double longitude) {
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setCubicCapacity(cubicCapacity);
        this.setPower(power);
        this.setMileage(mileage);
        this.setYearOfManufacture(yearOfManufacture);
        this.setFuel(fuel);
        this.setGearbox(gearbox);
        this.setEmissionClass(emissionClass);
        this.setVin(vin);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
}
