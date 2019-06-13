package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.payload.HouseRequest;

public class UpdateUtils {
    public static void updateHouseFromHouseRequest(House house, HouseRequest houseRequest) {
        house.setTitle(houseRequest.getTitle());
        house.setDescription(houseRequest.getDescription());
        house.setPrice(houseRequest.getPrice());
        house.setGardenSurface(houseRequest.getGardenSurface());
        house.setInternalSurface(houseRequest.getInternalSurface());
        house.setNumberOfBathrooms(houseRequest.getNumberOfBathrooms());
        house.setNumberOfRooms(houseRequest.getNumberOfRooms());
        house.setNumberOfFloors(houseRequest.getNumberOfFloors());
        house.setYearOfConstruction(houseRequest.getYearOfConstruction());
        house.setLatitude(houseRequest.getLatitude());
        house.setLongitude(houseRequest.getLongitude());
    }
}
