package com.raresopariuc.licenta.payload;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class CarResponse {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private List<String> pictureFileIds;
    private Integer cubicCapacity;
    private Integer power;
    private Integer mileage;
    private Integer yearOfManufacture;
    private String fuel;
    private String gearbox;
    private String emissionClass;
    private String vin;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Double latitude;
    private Double longitude;
}
