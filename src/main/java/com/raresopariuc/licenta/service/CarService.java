package com.raresopariuc.licenta.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raresopariuc.licenta.exception.BadRequestException;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.Car;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.CarRequest;
import com.raresopariuc.licenta.payload.CarResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.CarRepository;
import com.raresopariuc.licenta.repository.UserRepository;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.utils.AppConstants;
import com.raresopariuc.licenta.utils.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    public PagedResponse<CarResponse> getAllCars(UserPrincipal currentUser, int page, int size) {
        //validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Car> cars = carRepository.findAll(pageable);

        if(cars.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), cars.getNumber(), cars.getSize(),
                    cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
        }

        Map<Long, User> creatorMap = getCarCreatorMap(cars.getContent());

        List<CarResponse> carResponses = cars.map(car -> {
            return ModelMapper.mapCarToCarResponse(car, creatorMap.get(car.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(carResponses, cars.getNumber(), cars.getSize(),
                cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
    }

    public PagedResponse<CarResponse> getCarsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all cars created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Car> cars = carRepository.findByCreatedBy(user.getId(), pageable);

        if (cars.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), cars.getNumber(),
                    cars.getSize(), cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
        }

        List<CarResponse> carResponses = cars.map(car -> {
            return ModelMapper.mapCarToCarResponse(car, user);
        }).getContent();

        return new PagedResponse<>(carResponses, cars.getNumber(), cars.getSize(),
                cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
    }

    public PagedResponse<CarResponse> getCarsByFilter(UserPrincipal currentUser, int page, int size, String filters)
            throws IOException, JsonParseException {

        HashMap<String, String> result = new ObjectMapper().readValue(filters, HashMap.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Car> cars = carRepository.findCarsByFilter(
                result.get("cubicCapacityMin").equals("") ? null : Integer.parseInt(result.get("cubicCapacityMin")),
                result.get("cubicCapacityMax").equals("") ? null : Integer.parseInt(result.get("cubicCapacityMax")),
                result.get("powerMin").equals("") ? null : Integer.parseInt(result.get("powerMin")),
                result.get("powerMax").equals("") ? null : Integer.parseInt(result.get("powerMax")),
                result.get("mileageMin").equals("") ? null : Integer.parseInt(result.get("mileageMin")),
                result.get("mileageMax").equals("") ? null : Integer.parseInt(result.get("mileageMax")),
                result.get("yearOfManufactureMin").equals("") ? null : Integer.parseInt(result.get("yearOfManufactureMin")),
                result.get("yearOfManufactureMax").equals("") ? null : Integer.parseInt(result.get("yearOfManufactureMax")),
                result.get("priceMin").equals("") ? null : Integer.parseInt(result.get("priceMin")),
                result.get("priceMax").equals("") ? null : Integer.parseInt(result.get("priceMax")),
                pageable
        );

        if (cars.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), cars.getNumber(),
                    cars.getSize(), cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
        }

        Map<Long, User> creatorMap = getCarCreatorMap(cars.getContent());

        List<CarResponse> carResponses = cars.map(car -> {
            return ModelMapper.mapCarToCarResponse(car, creatorMap.get(car.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(carResponses, cars.getNumber(), cars.getSize(),
                cars.getTotalElements(), cars.getTotalPages(), cars.isLast());
    }

    public CarResponse getCarById(Long carId, UserPrincipal currentUser) {
        Car car = carRepository.findById(carId).orElseThrow(
                () -> new ResourceNotFoundException("Car", "id", carId));

        User creator = userRepository.findById(car.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", car.getCreatedBy()));

        return ModelMapper.mapCarToCarResponse(car, creator);

    }

    private Map<Long, User> getCarCreatorMap(List<Car> cars) {
        List<Long> creatorIds = cars.stream()
                .map(Car::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

    public Car createCar(CarRequest carRequest) {
        Car car = Car.builder()
                .title(carRequest.getTitle())
                .description(carRequest.getDescription())
                .price(carRequest.getPrice())
                .cubicCapacity(carRequest.getCubicCapacity())
                .power(carRequest.getPower())
                .mileage(carRequest.getMileage())
                .yearOfManufacture(carRequest.getYearOfManufacture())
                .fuel(carRequest.getFuel())
                .gearbox(carRequest.getGearbox())
                .emissionClass(carRequest.getEmissionClass())
                .vin(carRequest.getVin())
                .latitude(carRequest.getLatitude())
                .longitude(carRequest.getLongitude())
                .build();

        return carRepository.save(car);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
