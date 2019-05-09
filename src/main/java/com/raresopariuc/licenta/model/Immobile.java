package com.raresopariuc.licenta.model;

import com.raresopariuc.licenta.model.audit.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
