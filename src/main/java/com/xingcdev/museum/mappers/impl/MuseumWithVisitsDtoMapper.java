package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.MuseumWithVisitsDto;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MuseumWithVisitsDtoMapper implements IDtoMapper<Museum, MuseumWithVisitsDto> {

    private final ModelMapper mapper;

    public MuseumWithVisitsDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MuseumWithVisitsDto mapToDto(Museum museum) {
        return mapper.map(museum, MuseumWithVisitsDto.class);
    }

    @Override
    public Museum mapFromDto(MuseumWithVisitsDto museumWithVisitsDto) {
        return mapper.map(museumWithVisitsDto, Museum.class);
    }
}
