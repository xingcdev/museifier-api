package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.VisitedMuseumDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.mappers.ICustomPageDtoMapper;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class VisitedMuseumDtoMapper implements IDtoMapper<Museum, VisitedMuseumDto>, ICustomPageDtoMapper<Museum, VisitedMuseumDto> {

    private final ModelMapper mapper;

    public VisitedMuseumDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public VisitedMuseumDto mapToDto(Museum museum) {
        return mapper.map(museum, VisitedMuseumDto.class);
    }

    @Override
    public Museum mapFromDto(VisitedMuseumDto visitedMuseumDto) {
        return mapper.map(visitedMuseumDto, Museum.class);
    }

    @Override
    public CustomPage<VisitedMuseumDto> mapToCustomPageDto(CustomPage<Museum> museumPage) {
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
