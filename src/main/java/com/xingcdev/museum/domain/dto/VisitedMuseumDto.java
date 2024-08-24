package com.xingcdev.museum.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitedMuseumDto {

    private UUID id;

    private String name;

    private String address;

    private String postalCode;

    private String city;

    private String department;

    private String phoneNumber;

    private String url;

    private double latitude;

    private double longitude;

    private List<VisitedMuseumVisitsDto> visits;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class VisitedMuseumVisitsDto {

        private UUID id;

        private String title;

        private LocalDate visitDate;

        private int rating;

        private String comment;

        private Instant created;

        private Instant updated;
    }
}
