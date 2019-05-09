package com.raresopariuc.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "apartments")
@AllArgsConstructor
@Builder
public class Apartment extends Immobile {
    @NotNull
    @PositiveOrZero
    private Integer floorNumber;
}
