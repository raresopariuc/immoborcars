package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.UserSummary;

public class ModelMapper {

    public static HouseResponse mapHouseToHouseResponse(House house, User creator) {

        UserSummary creatorSummary = UserSummary.builder()
                .id(creator.getId())
                .username(creator.getUsername())
                .name(creator.getName())
                .build();

        return HouseResponse.builder()
                .id(house.getId())
                .title(house.getTitle())
                .pictureUrl(house.getPictureUrl())
                .internalSurface(house.getInternalSurface())
                .yearOfConstruction(house.getYearOfConstruction())
                .numberOfRooms(house.getNumberOfRooms())
                .numberOfBathrooms(house.getNumberOfBathrooms())
                .gardenSurface(house.getGardenSurface())
                .numberOfFloors(house.getNumberOfFloors())
                .createdBy(creatorSummary)
                .creationDateTime(house.getCreatedAt())
                .build();
    }

}
