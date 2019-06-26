package com.raresopariuc.licenta.model;

import com.raresopariuc.licenta.model.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
public class Announcement extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Lob
    private String description;

    @NotNull
    @Positive
    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DBFile> pictureFiles = new ArrayList<>();

    private Double latitude;

    private Double longitude;
}
