package com.raresopariuc.licenta.model;

import com.raresopariuc.licenta.model.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.*;

@MappedSuperclass
@Getter
@Setter
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
    private Integer yearOfConstruction;

    @NotNull
    @Positive
    private Integer numberOfRooms;

    @NotNull
    @Positive
    private Integer numberOfBathrooms;
}
