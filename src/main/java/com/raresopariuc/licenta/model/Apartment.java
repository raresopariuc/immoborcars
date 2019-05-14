package com.raresopariuc.licenta.model;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "apartments")
@AllArgsConstructor
public class Apartment extends Immobile {
    @NotNull
    @PositiveOrZero
    private Integer floorNumber;
}
