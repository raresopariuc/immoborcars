package com.raresopariuc.licenta.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raresopariuc.licenta.exception.BadRequestException;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.HouseRequest;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.HouseRepository;
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
public class HouseService {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(HouseService.class);

    public PagedResponse<HouseResponse> getAllHouses(UserPrincipal currentUser, int page, int size) {
        //validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<House> houses = houseRepository.findAll(pageable);

        if(houses.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), houses.getNumber(), houses.getSize(),
                    houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
        }

        Map<Long, User> creatorMap = getHouseCreatorMap(houses.getContent());

        List<HouseResponse> houseResponses = houses.map(house -> {
            return ModelMapper.mapHouseToHouseResponse(house, creatorMap.get(house.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(houseResponses, houses.getNumber(), houses.getSize(),
                houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
    }

    public PagedResponse<HouseResponse> getHousesCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all houses created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<House> houses = houseRepository.findByCreatedBy(user.getId(), pageable);

        if (houses.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), houses.getNumber(),
                    houses.getSize(), houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
        }

        List<HouseResponse> houseResponses = houses.map(house -> {
            return ModelMapper.mapHouseToHouseResponse(house, user);
        }).getContent();

        return new PagedResponse<>(houseResponses, houses.getNumber(), houses.getSize(),
                houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
    }

    public PagedResponse<HouseResponse> getHousesByFilter(UserPrincipal currentUser, int page, int size, String filters)
            throws IOException, JsonParseException {

        HashMap<String, String> result = new ObjectMapper().readValue(filters, HashMap.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<House> houses = houseRepository.findHousesByFilter(
                result.get("internalSurfaceMin").equals("") ? null : Integer.parseInt(result.get("internalSurfaceMin")),
                result.get("internalSurfaceMax").equals("") ? null : Integer.parseInt(result.get("internalSurfaceMax")),
                result.get("gardenSurfaceMin").equals("") ? null : Integer.parseInt(result.get("gardenSurfaceMin")),
                result.get("gardenSurfaceMax").equals("") ? null : Integer.parseInt(result.get("gardenSurfaceMax")),
                result.get("numberOfRoomsMin").equals("") ? null : Integer.parseInt(result.get("numberOfRoomsMin")),
                result.get("numberOfRoomsMax").equals("") ? null : Integer.parseInt(result.get("numberOfRoomsMax")),
                result.get("numberOfBathroomsMin").equals("") ? null : Integer.parseInt(result.get("numberOfBathroomsMin")),
                result.get("numberOfBathroomsMax").equals("") ? null : Integer.parseInt(result.get("numberOfBathroomsMax")),
                result.get("yearOfConstructionMin").equals("") ? null : Integer.parseInt(result.get("yearOfConstructionMin")),
                result.get("yearOfConstructionMax").equals("") ? null : Integer.parseInt(result.get("yearOfConstructionMax")),
                result.get("priceMin").equals("") ? null : Integer.parseInt(result.get("priceMin")),
                result.get("priceMax").equals("") ? null : Integer.parseInt(result.get("priceMax")),
                pageable
        );

        if (houses.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), houses.getNumber(),
                    houses.getSize(), houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
        }

        Map<Long, User> creatorMap = getHouseCreatorMap(houses.getContent());

        List<HouseResponse> houseResponses = houses.map(house -> {
            return ModelMapper.mapHouseToHouseResponse(house, creatorMap.get(house.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(houseResponses, houses.getNumber(), houses.getSize(),
                houses.getTotalElements(), houses.getTotalPages(), houses.isLast());
    }


    public HouseResponse getHouseById(Long houseId, UserPrincipal currentUser) {
        House house = houseRepository.findById(houseId).orElseThrow(
                () -> new ResourceNotFoundException("House", "id", houseId));

        User creator = userRepository.findById(house.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", house.getCreatedBy()));

        return ModelMapper.mapHouseToHouseResponse(house, creator);

    }

    private Map<Long, User> getHouseCreatorMap(List<House> houses) {
        List<Long> creatorIds = houses.stream()
                .map(House::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

    public House createHouse(HouseRequest houseRequest) {
        House house = House.builder()
                .title(houseRequest.getTitle())
                .description(houseRequest.getDescription())
                .price(houseRequest.getPrice())
                .internalSurface(houseRequest.getInternalSurface())
                .yearOfConstruction(houseRequest.getYearOfConstruction())
                .numberOfRooms(houseRequest.getNumberOfRooms())
                .numberOfBathrooms(houseRequest.getNumberOfBathrooms())
                .gardenSurface(houseRequest.getGardenSurface())
                .numberOfFloors(houseRequest.getNumberOfFloors())
                .latitude(houseRequest.getLatitude())
                .longitude(houseRequest.getLongitude())
                .build();

        return houseRepository.save(house);
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
