package com.xingcdev.museum.exceptions;

import lombok.Getter;

@Getter
public class MuseumAlreadyVisitedException extends RuntimeException {
    private final String code = "museum_already_visited";

    public MuseumAlreadyVisitedException(String name) {
        super(String.format("'%s' is already visited. Please provide the id of another museum.", name));
    }
}
