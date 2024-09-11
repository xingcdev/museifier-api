package com.xingcdev.museum.exceptions;

import lombok.Data;

@Data
public class InvalidSortingException extends RuntimeException {
    private String code = "invalid_sorting";

    public InvalidSortingException(String field) {
        super("Invalid sort field: " + field + ".");
    }
}
