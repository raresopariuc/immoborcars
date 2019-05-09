package com.raresopariuc.licenta.payload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HouseResponse {
    private Long id;
    private String title;
    private UserSummary createdBy;
}
