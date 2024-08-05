package com.xingcdev.museum.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "museum")
public class Museum {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    private String postalCode;

    private String city;

    private String department;

    private String phoneNumber;

    private String url;

    private double latitude;

    private double longitude;

    @OneToMany(mappedBy = "museum")
    private List<Visit> visits;

}
