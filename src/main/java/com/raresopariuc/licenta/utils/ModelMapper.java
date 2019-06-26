package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.*;
import com.raresopariuc.licenta.payload.ApartmentResponse;
import com.raresopariuc.licenta.payload.CarResponse;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.UserSummary;

import java.util.stream.Collectors;

public class ModelMapper {

    public static HouseResponse mapHouseToHouseResponse(House house, User creator) {

        UserSummary creatorSummary = UserSummary.builder()
                .id(creator.getId())
                .username(creator.getUsername())
                .name(creator.getName())
                .phoneNumber(creator.getPhoneNumber())
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
                .latitude(house.getLatitude())
                .longitude(house.getLongitude())
                .build();
    }

    public static ApartmentResponse mapApartmentToApartmentResponse(Apartment apartment, User creator) {

        UserSummary creatorSummary = UserSummary.builder()
                .id(creator.getId())
                .username(creator.getUsername())
                .name(creator.getName())
                .phoneNumber(creator.getPhoneNumber())
                .build();

        return ApartmentResponse.builder()
                .id(apartment.getId())
                .title(apartment.getTitle())
                .description(apartment.getDescription())
                .price(apartment.getPrice())
                .pictureFileIds(apartment.getPictureFiles().stream().map(DBFile::getId).collect(Collectors.toList()))
                .internalSurface(apartment.getInternalSurface())
                .yearOfConstruction(apartment.getYearOfConstruction())
                .numberOfRooms(apartment.getNumberOfRooms())
                .numberOfBathrooms(apartment.getNumberOfBathrooms())
                .floorNumber(apartment.getFloorNumber())
                .createdBy(creatorSummary)
                .creationDateTime(apartment.getCreatedAt())
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude())
                .build();
    }

    public static CarResponse mapCarToCarResponse(Car car, User creator) {

        UserSummary creatorSummary = UserSummary.builder()
                .id(creator.getId())
                .username(creator.getUsername())
                .name(creator.getName())
                .phoneNumber(creator.getPhoneNumber())
                .build();

        return CarResponse.builder()
                .id(car.getId())
                .title(car.getTitle())
                .description(car.getDescription())
                .price(car.getPrice())
                .pictureFileIds(car.getPictureFiles().stream().map(DBFile::getId).collect(Collectors.toList()))
                .cubicCapacity(car.getCubicCapacity())
                .power(car.getPower())
                .mileage(car.getMileage())
                .yearOfManufacture(car.getYearOfManufacture())
                .fuel(car.getFuel())
                .gearbox(car.getGearbox())
                .emissionClass(car.getEmissionClass())
                .vin(car.getVin())
                .createdBy(creatorSummary)
                .creationDateTime(car.getCreatedAt())
                .latitude(car.getLatitude())
                .longitude(car.getLongitude())
                .build();
    }

}
