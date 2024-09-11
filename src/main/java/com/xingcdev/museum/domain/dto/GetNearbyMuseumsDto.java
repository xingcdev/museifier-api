package com.xingcdev.museum.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetNearbyMuseumsDto {
    private double latitude;
    private double longitude;
    private List<NearbyMuseumDto> data;
    private int totalResults;
}
