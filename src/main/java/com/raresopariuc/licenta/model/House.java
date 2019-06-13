package com.raresopariuc.licenta.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Table(name = "houses")
@Getter
@Setter
@NoArgsConstructor
public class House extends Immobile {
    @NotNull
    @PositiveOrZero
    private Integer gardenSurface;

    @NotNull
    @PositiveOrZero
    private Integer numberOfFloors;

    @Builder
    public House(String title, String description, Integer price, Integer internalSurface, Integer yearOfConstruction, Integer numberOfRooms,
                 Integer numberOfBathrooms, Integer gardenSurface, Integer numberOfFloors, Double latitude, Double longitude) {
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setInternalSurface(internalSurface);
        this.setYearOfConstruction(yearOfConstruction);
        this.setNumberOfRooms(numberOfRooms);
        this.setNumberOfBathrooms(numberOfBathrooms);
        this.setGardenSurface(gardenSurface);
        this.setNumberOfFloors(numberOfFloors);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }
}
