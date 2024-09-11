package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.VisitRequestBody;
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
}
