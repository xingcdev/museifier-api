package com.xingcdev.museum.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ErrorDto {
    @Builder.Default
    private final String timestamp = new Date().toString();
    private final String code;
    private final String message;
}
