package com.raresopariuc.licenta.model;

import com.raresopariuc.licenta.model.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.*;

public abstract class Immobile extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    @Positive
    private Integer internalSurface;

    @NotNull
    @PositiveOrZero
    private Integer gardenSurface;

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
