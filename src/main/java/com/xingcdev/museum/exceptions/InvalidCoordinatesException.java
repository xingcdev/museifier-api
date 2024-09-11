package com.xingcdev.museum.exceptions;

import lombok.Data;

@Data
public class InvalidCoordinatesException extends RuntimeException {
    private String code = "invalid_coordinates";

    public InvalidCoordinatesException() {
        super("Please provide a valid format of coordinates. This must be specified as 'latitude,longitude'");
    }
}
