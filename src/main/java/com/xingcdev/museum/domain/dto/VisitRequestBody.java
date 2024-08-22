package com.xingcdev.museum.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitRequestBody {

    @NotBlank(message = "Please provide a title.")
    @Size(min = 2, max = 50, message = "The title must be between 2 and 50 characters long.")
    private String title;

    @NotNull(message = "Please provide a visit date.")
    @PastOrPresent(message = "Please provide a past date.")
    private LocalDate visitDate;

    @Min(value = 1, message = "A rating must be between 1 and 5.")
    @Max(value = 5, message = "A rating must be between 1 and 5.")
    private int rating;

    @NotBlank(message = "Please provide a comment.")
    @Size(min = 2, max = 255, message = "The comment must be between 2 and 255 characters long.")
    private String comment;

    @NotNull(message = "Please provide a museum Id.")
    private UUID museumId;
}
