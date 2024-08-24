package com.xingcdev.museum.mappers;

import com.xingcdev.museum.domain.entities.CustomPage;

public interface IDtoMapper<Entity, Dto> {

    Dto mapToDto(Entity a);

    Entity mapFromDto(Dto b);

    CustomPage<Dto> mapToCustomPageDto(CustomPage<Entity> entityPage);
}
