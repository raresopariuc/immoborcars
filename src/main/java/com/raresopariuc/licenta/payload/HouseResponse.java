package com.raresopariuc.licenta.payload;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class HouseResponse {
    private Long id;
    private String title;
    private String pictureUrl;
    private Integer internalSurface;
    private Integer yearOfConstruction;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Integer gardenSurface;
    private Integer numberOfFloors;
    private UserSummary createdBy;
    private Instant creationDateTime;
}
