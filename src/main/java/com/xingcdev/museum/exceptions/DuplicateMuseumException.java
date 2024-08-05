package com.xingcdev.museum.exceptions;

import lombok.Data;

@Data
public class DuplicateMuseumException extends RuntimeException {
    private String code = "duplicateMuseum";
    public DuplicateMuseumException(String name) {
        super(String.format("'%s' is already visited. Please provide the id of other museum.", name));
    }
}
