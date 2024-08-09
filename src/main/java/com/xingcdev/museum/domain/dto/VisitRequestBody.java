package com.xingcdev.museum.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitRequestBody {

    @NotBlank(message = "Please provide a comment")
    private String comment;

    @NotNull(message = "Please provide a museum Id")
    private UUID museumId;
}
