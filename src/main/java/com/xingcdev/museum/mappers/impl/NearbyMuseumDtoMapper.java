package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.GetNearbyMuseumsDto;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.mappers.IDtoMapper;

public class NearbyMuseumDtoMapper implements IDtoMapper<Museum, GetNearbyMuseumsDto.NearbyMuseumDto> {

    @Override
    public GetNearbyMuseumsDto.NearbyMuseumDto mapToDto(Museum a) {
        return null;
    }

    @Override
    public Museum mapFromDto(GetNearbyMuseumsDto.NearbyMuseumDto b) {
        return null;
    }
}
