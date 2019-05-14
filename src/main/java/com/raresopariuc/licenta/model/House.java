package com.raresopariuc.licenta.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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
    public House(String title, Integer internalSurface, Integer yearOfConstruction, Integer numberOfRooms,
                 Integer numberOfBathrooms, Integer gardenSurface, Integer numberOfFloors) {
        this.setTitle(title);
        this.setInternalSurface(internalSurface);
        this.setYearOfConstruction(yearOfConstruction);
        this.setNumberOfRooms(numberOfRooms);
        this.setNumberOfBathrooms(numberOfBathrooms);
        this.setGardenSurface(gardenSurface);
        this.setNumberOfFloors(numberOfFloors);
    }
}
