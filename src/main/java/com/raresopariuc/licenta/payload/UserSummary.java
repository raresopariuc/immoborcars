package com.raresopariuc.licenta.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private String phoneNumber;
}
