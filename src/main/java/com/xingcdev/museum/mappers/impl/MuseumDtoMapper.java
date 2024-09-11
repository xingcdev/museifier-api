package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.MuseumDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.mappers.ICustomPageDtoMapper;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MuseumDtoMapper implements IDtoMapper<Museum, MuseumDto>, ICustomPageDtoMapper<Museum, MuseumDto> {

    private final ModelMapper mapper;

    public MuseumDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MuseumDto mapToDto(Museum museum) {
        return this.mapper.map(museum, MuseumDto.class);
    }

    @Override
    public Museum mapFromDto(MuseumDto museumDto) {
        return this.mapper.map(museumDto, Museum.class);
    }

    @Override
    public CustomPage<MuseumDto> mapToCustomPageDto(CustomPage<Museum> museumPage) {
        var museumDtoList = museumPage.getData().stream().map(this::mapToDto).toList();
        return new CustomPage<>(
                museumDtoList,
                museumPage.getPageInfo().getPage(),
                museumPage.getPageInfo().getSize(),
                museumPage.getPageInfo().getTotalResults(),
                museumPage.getPageInfo().getTotalPages(),
                museumPage.getPageInfo().isLast()
        );
    }
}
