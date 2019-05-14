package com.raresopariuc.licenta.controller;

import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.payload.ApiResponse;
import com.raresopariuc.licenta.payload.HouseRequest;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/houses")
public class HouseController {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseService houseService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseController.class);

    @GetMapping
    public PagedResponse<HouseResponse> getHouses(@CurrentUser UserPrincipal currentUser,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "30") int size) {
        return houseService.getAllHouses(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createHouse(@Valid @RequestBody HouseRequest houseRequest) {
        House house = houseService.createHouse(houseRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{houseId}")
                .buildAndExpand(house.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "House successfully created!"));
    }
}
