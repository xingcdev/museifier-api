package com.xingcdev.museum.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DuplicateMuseumException extends RuntimeException {
    private String code = "duplicate_museum";

    public DuplicateMuseumException(String name) {
        super(String.format("'%s' is already visited. Please provide the id of other museum.", name));
    }
}
