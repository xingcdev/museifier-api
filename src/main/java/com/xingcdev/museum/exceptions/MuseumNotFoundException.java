package com.xingcdev.museum.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MuseumNotFoundException extends RuntimeException {
    private String code = "museum_not_found";

    public MuseumNotFoundException(String id) {
        super("Could not find museum " + id + ". Please provide a valid id.");
    }
}
