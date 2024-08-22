package com.xingcdev.museum.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visit")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private LocalDate visitDate;

    private String comment;

    private int rating;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "museum_id")
    private Museum museum;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant updated;

}
