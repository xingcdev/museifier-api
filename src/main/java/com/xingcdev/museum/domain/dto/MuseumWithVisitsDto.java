package com.xingcdev.museum.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MuseumWithVisitsDto {
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

    private List<MuseumVisit> visits;

    @Data
    @NoArgsConstructor
    static class MuseumVisit {
        private UUID id;

        private String title;

        private LocalDate visitDate;

        private int rating;

        private String comment;

        private Instant created;

        private Instant updated;
    }
}
