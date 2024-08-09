package com.xingcdev.museum.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ErrorDto {
    @Builder.Default
    private String timestamp = new Date().toString();
    private String code;
    private String message;
    @Builder.Default
    private List<DetailDto> details = new ArrayList<>();
}
