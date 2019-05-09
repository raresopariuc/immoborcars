package com.raresopariuc.licenta.service;

import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.PagedResponse;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.repository.UserRepository;
import com.raresopariuc.licenta.security.UserPrincipal;
import com.raresopariuc.licenta.utils.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}
