package com.raresopariuc.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "houses")
@AllArgsConstructor
@Builder
public class House extends Immobile {
    @NotNull
    @PositiveOrZero
    private Integer gardenSurface;

    @NotNull
    @PositiveOrZero
    private Integer numberOfFloors;
}
