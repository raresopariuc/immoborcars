package com.raresopariuc.licenta.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raresopariuc.licenta.exception.BadRequestException;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.Apartment;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.ApartmentRequest;
import com.raresopariuc.licenta.payload.ApartmentResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.ApartmentRepository;
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
public class ApartmentService {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApartmentService.class);

    public PagedResponse<ApartmentResponse> getAllApartments(UserPrincipal currentUser, int page, int size) {
        //validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Apartment> apartments = apartmentRepository.findAll(pageable);

        if(apartments.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), apartments.getNumber(), apartments.getSize(),
                    apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
        }

        Map<Long, User> creatorMap = getApartmentCreatorMap(apartments.getContent());

        List<ApartmentResponse> apartmentResponses = apartments.map(apartment -> {
            return ModelMapper.mapApartmentToApartmentResponse(apartment, creatorMap.get(apartment.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(apartmentResponses, apartments.getNumber(), apartments.getSize(),
                apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
    }

    public PagedResponse<ApartmentResponse> getApartmentsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all apartments created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Apartment> apartments = apartmentRepository.findByCreatedBy(user.getId(), pageable);

        if (apartments.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), apartments.getNumber(),
                    apartments.getSize(), apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
        }

        List<ApartmentResponse> apartmentResponses = apartments.map(apartment -> {
            return ModelMapper.mapApartmentToApartmentResponse(apartment, user);
        }).getContent();

        return new PagedResponse<>(apartmentResponses, apartments.getNumber(), apartments.getSize(),
                apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
    }

    public PagedResponse<ApartmentResponse> getApartmentsByFilter(UserPrincipal currentUser, int page, int size, String filters)
            throws IOException, JsonParseException {

        HashMap<String, String> result = new ObjectMapper().readValue(filters, HashMap.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Apartment> apartments = apartmentRepository.findApartmentsByFilter(
                result.get("internalSurfaceMin").equals("") ? null : Integer.parseInt(result.get("internalSurfaceMin")),
                result.get("internalSurfaceMax").equals("") ? null : Integer.parseInt(result.get("internalSurfaceMax")),
                result.get("floorNumberMin").equals("") ? null : Integer.parseInt(result.get("floorNumberMin")),
                result.get("floorNumberMax").equals("") ? null : Integer.parseInt(result.get("floorNumberMax")),
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

        if (apartments.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), apartments.getNumber(),
                    apartments.getSize(), apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
        }

        Map<Long, User> creatorMap = getApartmentCreatorMap(apartments.getContent());

        List<ApartmentResponse> apartmentResponses = apartments.map(apartment -> {
            return ModelMapper.mapApartmentToApartmentResponse(apartment, creatorMap.get(apartment.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(apartmentResponses, apartments.getNumber(), apartments.getSize(),
                apartments.getTotalElements(), apartments.getTotalPages(), apartments.isLast());
    }

    public ApartmentResponse getApartmentById(Long apartmentId, UserPrincipal currentUser) {
        Apartment apartment = apartmentRepository.findById(apartmentId).orElseThrow(
                () -> new ResourceNotFoundException("Apartment", "id", apartmentId));

        User creator = userRepository.findById(apartment.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", apartment.getCreatedBy()));

        return ModelMapper.mapApartmentToApartmentResponse(apartment, creator);

    }

    private Map<Long, User> getApartmentCreatorMap(List<Apartment> apartments) {
        List<Long> creatorIds = apartments.stream()
                .map(Apartment::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }

    public Apartment createApartment(ApartmentRequest apartmentRequest) {
        Apartment apartment = Apartment.builder()
                .title(apartmentRequest.getTitle())
                .description(apartmentRequest.getDescription())
                .price(apartmentRequest.getPrice())
                .internalSurface(apartmentRequest.getInternalSurface())
                .yearOfConstruction(apartmentRequest.getYearOfConstruction())
                .numberOfRooms(apartmentRequest.getNumberOfRooms())
                .numberOfBathrooms(apartmentRequest.getNumberOfBathrooms())
                .floorNumber(apartmentRequest.getFloorNumber())
                .latitude(apartmentRequest.getLatitude())
                .longitude(apartmentRequest.getLongitude())
                .build();

        return apartmentRepository.save(apartment);
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
