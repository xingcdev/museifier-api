package com.xingcdev.museum.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NearbyMuseumDto extends MuseumDto {
    private int totalVisits;
    private double distance;
}
