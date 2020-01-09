package com.raresopariuc.licenta.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
public class HouseRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    @Valid
    private Integer price;

    @NotNull
    @Valid
    private Integer internalSurface;

    @NotNull
    @Valid
    private Integer yearOfConstruction;

    @NotNull
    @Valid
    private Integer numberOfRooms;

    @NotNull
    @Valid
    private Integer numberOfBathrooms;

    @NotNull
    @Valid
    private Integer gardenSurface;

    @NotNull
    @Valid
    private Integer numberOfFloors;

    private Double latitude;

    private Double longitude;
}
