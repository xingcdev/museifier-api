package com.xingcdev.museum.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDto {

    private UUID id;

    private String title;

    private LocalDate visitDate;

    private int rating;

    private String comment;

    private MuseumDto museum;

    private Instant created;

    private Instant updated;
}
