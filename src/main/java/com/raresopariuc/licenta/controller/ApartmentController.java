package com.raresopariuc.licenta.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.Apartment;
import com.raresopariuc.licenta.model.DBFile;
import com.raresopariuc.licenta.payload.ApartmentRequest;
import com.raresopariuc.licenta.payload.ApartmentResponse;
import com.raresopariuc.licenta.payload.ApiResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.ApartmentRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.ApartmentService;
import com.raresopariuc.licenta.service.DBFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.raresopariuc.licenta.utils.UpdateUtils.updateApartmentFromApartmentRequest;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentController.class);

    @GetMapping
    public PagedResponse<ApartmentResponse> getApartments(@CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size) {
        return apartmentService.getAllApartments(currentUser, page, size);
    }

    @GetMapping("/{apartmentId}")
    public ApartmentResponse getApartmentById(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable Long apartmentId) {
        return apartmentService.getApartmentById(apartmentId, currentUser);
    }

    @PostMapping("/searchByFilter")
    public PagedResponse<ApartmentResponse> getHousesByFilter(@CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size,
                                                          @RequestBody String filters) throws IOException, JsonParseException {
        return apartmentService.getApartmentsByFilter(currentUser, page, size, filters);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createApartment(@Valid @RequestBody ApartmentRequest apartmentRequest) {
        Apartment apartment = apartmentService.createApartment(apartmentRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{apartmentId}")
                .buildAndExpand(apartment.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Apartment successfully created!", apartment.getId().toString()));
    }

    @DeleteMapping("/{apartmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteApartment(@CurrentUser UserPrincipal currentUser,
                                             @PathVariable Long apartmentId) {

        if(currentUser.getUsername().equals(
                apartmentService.getApartmentById(apartmentId, currentUser).getCreatedBy().getUsername())) {
            apartmentRepository.deleteById(apartmentId);
        }

        return apartmentRepository.findById(apartmentId).isPresent() ?
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Apartment wasn't deleted!", apartmentId.toString())) :
                ResponseEntity.ok()
                        .body(new ApiResponse(true, "Apartment successfully deleted!", apartmentId.toString()));
    }

    @PutMapping("/{apartmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateApartment(@Valid @RequestBody ApartmentRequest apartmentRequest, @PathVariable Long apartmentId) {

        Apartment currentApartment = apartmentRepository.findById(apartmentId).orElseThrow(
                () -> new ResourceNotFoundException("Apartment", "id", apartmentId));

        updateApartmentFromApartmentRequest(currentApartment, apartmentRequest);

        Apartment updatedApartment = apartmentRepository.save(currentApartment);

        return ResponseEntity.ok().body(updatedApartment);
    }

    @PostMapping("/pictures/{apartmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPicture(@CurrentUser UserPrincipal currentUser, @PathVariable Long apartmentId, @RequestBody String pictureFileId) {

        Apartment apartment = apartmentRepository.findById(apartmentId).orElseThrow(
                () -> new ResourceNotFoundException("Apartment", "id", apartmentId));

        List<DBFile> pictureFiles = apartment.getPictureFiles();
        pictureFiles.add(dbFileStorageService.getFile(pictureFileId));

        apartment.setPictureFiles(pictureFiles);

        Apartment updatedApartment = apartmentRepository.save(apartment);

        return ResponseEntity.ok().body(updatedApartment);
    }
}
