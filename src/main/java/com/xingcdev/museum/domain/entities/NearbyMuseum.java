package com.xingcdev.museum.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class NearbyMuseum extends Museum {

    private int totalVisits;

    private double distance;

    private int averageRating;
}
