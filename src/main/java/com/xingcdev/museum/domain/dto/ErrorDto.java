package com.xingcdev.museum.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorDto {
    @Builder.Default
    private final String timestamp = LocalDateTime.now().toString();
    private final String code;
    private final String message;
}
