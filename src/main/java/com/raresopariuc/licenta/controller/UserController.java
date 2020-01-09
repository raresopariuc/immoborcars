package com.raresopariuc.licenta.controller;

import com.raresopariuc.licenta.exception.ResourceNotFoundException;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.*;
import com.raresopariuc.licenta.repository.ApartmentRepository;
import com.raresopariuc.licenta.repository.CarRepository;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.repository.UserRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.ApartmentService;
import com.raresopariuc.licenta.service.CarService;
import com.raresopariuc.licenta.service.HouseService;
import com.raresopariuc.licenta.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private CarService carService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
                currentUser.getName(), currentUser.getPhoneNumber());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long houseCount = houseRepository.countByCreatedBy(user.getId());
        long apartmentCount = apartmentRepository.countByCreatedBy(user.getId());
        long carCount = carRepository.countByCreatedBy(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(),
                houseCount, apartmentCount, carCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/houses")
    public PagedResponse<HouseResponse> getHousesCreatedBy(@PathVariable(value = "username") String username,
                                                           @CurrentUser UserPrincipal currentUser,
                                                           @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return houseService.getHousesCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/apartments")
    public PagedResponse<ApartmentResponse> getApartmentsCreatedBy(@PathVariable(value = "username") String username,
                                                           @CurrentUser UserPrincipal currentUser,
                                                           @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return apartmentService.getApartmentsCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/cars")
    public PagedResponse<CarResponse> getCarsCreatedBy(@PathVariable(value = "username") String username,
                                                           @CurrentUser UserPrincipal currentUser,
                                                           @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                           @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return carService.getCarsCreatedBy(username, currentUser, page, size);
    }
}