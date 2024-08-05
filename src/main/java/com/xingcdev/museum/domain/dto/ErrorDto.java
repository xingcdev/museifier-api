package com.xingcdev.museum.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String timestamp;
    private String code;
    private String message;
}
