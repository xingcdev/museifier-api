package com.xingcdev.museum.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ErrorDtoWithDetails {
    @Builder.Default
    private final String timestamp = new Date().toString();
    private final String code;
    private final String message;
    private final List<DetailDto> details;
}
