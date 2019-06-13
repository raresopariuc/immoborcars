package com.raresopariuc.licenta.controller;

import com.google.common.collect.Lists;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.DBFile;
import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.payload.ApiResponse;
import com.raresopariuc.licenta.payload.HouseRequest;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.DBFileStorageService;
import com.raresopariuc.licenta.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.raresopariuc.licenta.utils.UpdateUtils.updateHouseFromHouseRequest;

@RestController
@RequestMapping("/api/houses")
public class HouseController {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseService houseService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseController.class);

    @GetMapping
    public PagedResponse<HouseResponse> getHouses(@CurrentUser UserPrincipal currentUser,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "30") int size) {
        return houseService.getAllHouses(currentUser, page, size);
    }

    @GetMapping("/{houseId}")
    public HouseResponse getHouseById(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable Long houseId) {
        return houseService.getHouseById(houseId, currentUser);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createHouse(@Valid @RequestBody HouseRequest houseRequest) {
        House house = houseService.createHouse(houseRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{houseId}")
                .buildAndExpand(house.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "House successfully created!", house.getId().toString()));
    }

    @DeleteMapping("/{houseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteHouse(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long houseId) {

        if(currentUser.getUsername().equals(
                houseService.getHouseById(houseId, currentUser).getCreatedBy().getUsername())) {
            houseRepository.deleteById(houseId);
        }

        return houseRepository.findById(houseId).isPresent() ?
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "House wasn't deleted!", houseId.toString())) :
                ResponseEntity.ok()
                    .body(new ApiResponse(true, "House successfully deleted!", houseId.toString()));
    }

    @PutMapping("/{houseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateHouse(@Valid @RequestBody HouseRequest houseRequest, @PathVariable Long houseId) {

        House currentHouse = houseRepository.findById(houseId).orElseThrow(
                () -> new ResourceNotFoundException("House", "id", houseId));


        updateHouseFromHouseRequest(currentHouse, houseRequest);

        House updatedHouse = houseRepository.save(currentHouse);

        return ResponseEntity.ok().body(updatedHouse);
    }

    @PostMapping("/pictures/{houseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPicture(@CurrentUser UserPrincipal currentUser, @PathVariable Long houseId, @RequestBody String pictureFileId) {

        House house = houseRepository.findById(houseId).orElseThrow(
                () -> new ResourceNotFoundException("House", "id", houseId));

        List<DBFile> pictureFiles = house.getPictureFiles();
        pictureFiles.add(dbFileStorageService.getFile(pictureFileId));

        house.setPictureFiles(pictureFiles);

        House updatedHouse = houseRepository.save(house);

        return ResponseEntity.ok().body(updatedHouse);
    }
}
