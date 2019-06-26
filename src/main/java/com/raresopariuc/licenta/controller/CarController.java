package com.raresopariuc.licenta.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.Car;
import com.raresopariuc.licenta.model.DBFile;
import com.raresopariuc.licenta.payload.ApiResponse;
import com.raresopariuc.licenta.payload.CarRequest;
import com.raresopariuc.licenta.payload.CarResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.CarRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.CarService;
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

import static com.raresopariuc.licenta.utils.UpdateUtils.updateCarFromCarRequest;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

    @GetMapping
    public PagedResponse<CarResponse> getCars(@CurrentUser UserPrincipal currentUser,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "30") int size) {
        return carService.getAllCars(currentUser, page, size);
    }

    @GetMapping("/{carId}")
    public CarResponse getCarById(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long carId) {
        return carService.getCarById(carId, currentUser);
    }

    @PostMapping("/searchByFilter")
    public PagedResponse<CarResponse> getHousesByFilter(@CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size,
                                                          @RequestBody String filters) throws IOException, JsonParseException {
        return carService.getCarsByFilter(currentUser, page, size, filters);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCar(@Valid @RequestBody CarRequest carRequest) {
        Car car = carService.createCar(carRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{carId}")
                .buildAndExpand(car.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Car successfully created!", car.getId().toString()));
    }

    @DeleteMapping("/{carId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCar(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable Long carId) {

        if(currentUser.getUsername().equals(
                carService.getCarById(carId, currentUser).getCreatedBy().getUsername())) {
            carRepository.deleteById(carId);
        }

        return carRepository.findById(carId).isPresent() ?
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Car wasn't deleted!", carId.toString())) :
                ResponseEntity.ok()
                        .body(new ApiResponse(true, "Car successfully deleted!", carId.toString()));
    }

    @PutMapping("/{carId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateCar(@Valid @RequestBody CarRequest carRequest, @PathVariable Long carId) {

        Car currentCar = carRepository.findById(carId).orElseThrow(
                () -> new ResourceNotFoundException("Car", "id", carId));


        updateCarFromCarRequest(currentCar, carRequest);

        Car updatedCar = carRepository.save(currentCar);

        return ResponseEntity.ok().body(updatedCar);
    }

    @PostMapping("/pictures/{carId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPicture(@CurrentUser UserPrincipal currentUser, @PathVariable Long carId, @RequestBody String pictureFileId) {

        Car car = carRepository.findById(carId).orElseThrow(
                () -> new ResourceNotFoundException("Car", "id", carId));

        List<DBFile> pictureFiles = car.getPictureFiles();
        pictureFiles.add(dbFileStorageService.getFile(pictureFileId));

        car.setPictureFiles(pictureFiles);

        Car updatedCar = carRepository.save(car);

        return ResponseEntity.ok().body(updatedCar);
    }
}
