package com.xingcdev.museum.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MuseumDto {

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
}
