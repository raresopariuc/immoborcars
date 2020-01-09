package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.Apartment;
import com.raresopariuc.licenta.model.Car;
import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.payload.ApartmentRequest;
import com.raresopariuc.licenta.payload.CarRequest;
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
        if(houseRequest.getLatitude() != null && houseRequest.getLongitude() != null) {
            house.setLatitude(houseRequest.getLatitude());
            house.setLongitude(houseRequest.getLongitude());
        }
    }

    public static void updateApartmentFromApartmentRequest(Apartment apartment, ApartmentRequest apartmentRequest) {
        apartment.setTitle(apartmentRequest.getTitle());
        apartment.setDescription(apartmentRequest.getDescription());
        apartment.setPrice(apartmentRequest.getPrice());
        apartment.setInternalSurface(apartmentRequest.getInternalSurface());
        apartment.setNumberOfBathrooms(apartmentRequest.getNumberOfBathrooms());
        apartment.setNumberOfRooms(apartmentRequest.getNumberOfRooms());
        apartment.setFloorNumber(apartmentRequest.getFloorNumber());
        apartment.setYearOfConstruction(apartmentRequest.getYearOfConstruction());
        if(apartmentRequest.getLatitude() != null && apartmentRequest.getLongitude() != null) {
            apartment.setLatitude(apartmentRequest.getLatitude());
            apartment.setLongitude(apartmentRequest.getLongitude());
        }
    }

    public static void updateCarFromCarRequest(Car car, CarRequest carRequest) {
        car.setTitle(carRequest.getTitle());
        car.setDescription(carRequest.getDescription());
        car.setPrice(carRequest.getPrice());
        car.setCubicCapacity(carRequest.getCubicCapacity());
        car.setPower(carRequest.getPower());
        car.setMileage(carRequest.getMileage());
        car.setYearOfManufacture(carRequest.getYearOfManufacture());
        car.setFuel(carRequest.getFuel());
        car.setGearbox(carRequest.getGearbox());
        car.setEmissionClass(carRequest.getEmissionClass());
        car.setVin(carRequest.getVin());
        if(carRequest.getLatitude() != null && carRequest.getLongitude() != null) {
            car.setLatitude(carRequest.getLatitude());
            car.setLongitude(carRequest.getLongitude());
        }
    }
}
