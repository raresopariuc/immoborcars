package com.raresopariuc.licenta.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@MappedSuperclass
@Getter
@Setter
public abstract class Immobile extends Announcement {

    @NotNull
    @Positive
    private Integer internalSurface;

    @NotNull
    @PositiveOrZero
    private Integer yearOfConstruction;

    @NotNull
    @Positive
    private Integer numberOfRooms;

    @NotNull
    @Positive
    private Integer numberOfBathrooms;
}
