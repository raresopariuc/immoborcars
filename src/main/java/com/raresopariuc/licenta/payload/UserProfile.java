package com.raresopariuc.licenta.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
}
