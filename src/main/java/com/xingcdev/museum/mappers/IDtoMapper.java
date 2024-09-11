package com.xingcdev.museum.mappers;

public interface IDtoMapper<Entity, Dto> {

    Dto mapToDto(Entity a);

    Entity mapFromDto(Dto b);


}
