package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.NearbyMuseumDto;
import com.xingcdev.museum.domain.entities.NearbyMuseum;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NearbyMuseumDtoMapper implements IDtoMapper<NearbyMuseum, NearbyMuseumDto> {

    private final ModelMapper mapper;

    public NearbyMuseumDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public NearbyMuseumDto mapToDto(NearbyMuseum nearbyMuseum) {
        return mapper.map(nearbyMuseum, NearbyMuseumDto.class);
    }

    @Override
    public NearbyMuseum mapFromDto(NearbyMuseumDto nearbyMuseumDto) {
        return mapper.map(nearbyMuseumDto, NearbyMuseum.class);
    }
}
