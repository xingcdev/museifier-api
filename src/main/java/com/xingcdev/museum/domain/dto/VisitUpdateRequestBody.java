package com.xingcdev.museum.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitUpdateRequestBody {

    @NotBlank(message = "Please provide a title.")
    @Size(min = 2, max = 50, message = "The title must be between 2 and 50 characters long.")
    private String title;

    @NotBlank(message = "Please provide a comment.")
    @Size(min = 2, max = 255, message = "The comment must be between 2 and 255 characters long.")
    private String comment;

}
