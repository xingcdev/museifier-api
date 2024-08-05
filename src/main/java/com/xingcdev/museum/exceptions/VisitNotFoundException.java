package com.xingcdev.museum.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class VisitNotFoundException extends RuntimeException {
    private String code = "visitNotFound";
    public VisitNotFoundException(UUID id) {
        super("Could not find visit " + id + ". Please provide a valid id.");
    }
}
