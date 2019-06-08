package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.DBFile;
import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.UserSummary;

import java.util.stream.Collectors;

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
                .description(house.getDescription())
                .price(house.getPrice())
                .pictureFileIds(house.getPictureFiles().stream().map(DBFile::getId).collect(Collectors.toList()))
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
