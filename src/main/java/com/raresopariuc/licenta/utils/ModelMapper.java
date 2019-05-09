package com.raresopariuc.licenta.utils;

import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.model.User;
import com.raresopariuc.licenta.payload.HouseResponse;
import com.raresopariuc.licenta.payload.UserSummary;

public class ModelMapper {

    public static HouseResponse mapHouseToHouseResponse(House house, User creator) {

        UserSummary creatorSummary = UserSummary.builder()
                .id(creator.getId())
                .username(creator.getUsername())
                .name(creator.getName())
                .build();

        return HouseResponse.builder()
                .id(house.getId())
                .title(house.getTitle())
                .createdBy(creatorSummary)
                .build();
    }

}
