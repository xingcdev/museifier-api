package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.VisitRequestBody;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisitRequestBodyMapper implements IDtoMapper<Visit, VisitRequestBody> {

    private final ModelMapper mapper;

    public VisitRequestBodyMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public VisitRequestBody mapToDto(Visit visit) {
        return this.mapper.map(visit, VisitRequestBody.class);
    }

    @Override
    public Visit mapFromDto(VisitRequestBody visitRequestBody) {
        return this.mapper.map(visitRequestBody, Visit.class);
    }

    @Override
    public CustomPage<VisitRequestBody> mapToCustomPageDto(CustomPage<Visit> visitPage) {
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
