package com.raresopariuc.licenta.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
public class Apartment extends Immobile {
    @NotNull
    @PositiveOrZero
    private Integer floorNumber;

    @Builder
    public Apartment(String title, String description, Integer price, Integer internalSurface, Integer yearOfConstruction,
                     Integer numberOfRooms, Integer numberOfBathrooms, Integer floorNumber, Double latitude, Double longitude) {
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setInternalSurface(internalSurface);
        this.setYearOfConstruction(yearOfConstruction);
        this.setNumberOfRooms(numberOfRooms);
        this.setNumberOfBathrooms(numberOfBathrooms);
        this.setFloorNumber(floorNumber);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
}
