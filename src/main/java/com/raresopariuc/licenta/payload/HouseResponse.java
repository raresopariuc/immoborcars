package com.raresopariuc.licenta.payload;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class HouseResponse {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private List<String> pictureFileIds;
    private Integer internalSurface;
    private Integer yearOfConstruction;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Integer gardenSurface;
    private Integer numberOfFloors;
    private UserSummary createdBy;
    private Instant creationDateTime;
}
