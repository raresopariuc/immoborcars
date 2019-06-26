package com.raresopariuc.licenta.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CarRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    @Valid
    private Integer price;

    @NotNull
    @Valid
    private Integer cubicCapacity;

    @NotNull
    @Valid
    private Integer power;

    @NotNull
    @Valid
    private Integer mileage;

    @NotNull
    @Valid
    private Integer yearOfManufacture;

    @NotBlank
    @Size(max = 40)
    private String fuel;

    @NotBlank
    @Size(max = 40)
    private String gearbox;

    @NotBlank
    @Size(max = 40)
    private String emissionClass;

    @Size(max = 40)
    private String vin;

    private Double latitude;

    private Double longitude;
}
