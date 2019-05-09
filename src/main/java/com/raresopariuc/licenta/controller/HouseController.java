package com.raresopariuc.licenta.controller;

import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.security.CurrentUser;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
