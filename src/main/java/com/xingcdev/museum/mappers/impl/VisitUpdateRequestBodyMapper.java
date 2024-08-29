package com.xingcdev.museum.mappers.impl;

import com.xingcdev.museum.domain.dto.VisitUpdateRequestBody;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.mappers.IDtoMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisitUpdateRequestBodyMapper implements IDtoMapper<Visit, VisitUpdateRequestBody> {

    private final ModelMapper mapper;

    public VisitUpdateRequestBodyMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public VisitUpdateRequestBody mapToDto(Visit visit) {
        return this.mapper.map(visit, VisitUpdateRequestBody.class);
    }

    @Override
    public Visit mapFromDto(VisitUpdateRequestBody visitUpdateRequestBody) {
        return this.mapper.map(visitUpdateRequestBody, Visit.class);
    }

    @Override
    public CustomPage<VisitUpdateRequestBody> mapToCustomPageDto(CustomPage<Visit> entityPage) {
        return null;
    }


}
