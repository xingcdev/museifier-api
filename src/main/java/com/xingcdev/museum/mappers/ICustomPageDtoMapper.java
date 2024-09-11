package com.xingcdev.museum.mappers;

import com.xingcdev.museum.domain.entities.CustomPage;

public interface ICustomPageDtoMapper<Entity, Dto> {
    CustomPage<Dto> mapToCustomPageDto(CustomPage<Entity> entityPage);
}
