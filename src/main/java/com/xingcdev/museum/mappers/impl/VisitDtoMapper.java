package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.VisitDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisitDtoMapper implements IDtoMapper<Visit, VisitDto> {

    private final ModelMapper mapper;

    public VisitDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public VisitDto mapToDto(Visit visit) {
        return this.mapper.map(visit, VisitDto.class);
    }

    @Override
    public Visit mapFromDto(VisitDto visitDto) {
        return this.mapper.map(visitDto, Visit.class);
    }

    @Override
    public CustomPage<VisitDto> mapToCustomPageDto(CustomPage<Visit> visitPage) {
        var museumDtoList = visitPage.getData().stream().map(this::mapToDto).toList();
        return new CustomPage<>(
                museumDtoList,
                visitPage.getPageInfo().getPage(),
                visitPage.getPageInfo().getSize(),
                visitPage.getPageInfo().getTotalResults(),
                visitPage.getPageInfo().getTotalPages(),
                visitPage.getPageInfo().isLast()
        );
    }
}
