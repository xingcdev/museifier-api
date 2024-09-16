package com.xingcdev.museum.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidSortingException extends RuntimeException {
    private String code = "invalid_sorting";

    public InvalidSortingException(String field) {
        super("Invalid sort field: " + field + ".");
    }
}
