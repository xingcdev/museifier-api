package com.xingcdev.museum.repositories;

import com.xingcdev.museum.domain.entities.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitRepository extends CrudRepository<Visit, UUID>, PagingAndSortingRepository<Visit, UUID> {

    Page<Visit> findByUserId(String userId, Pageable pageable);

    Optional<Visit> findByIdAndUserId(UUID id, String userId);

    boolean existsByMuseumIdAndUserId(UUID museumId, String userId);

    boolean existsByIdAndUserId(UUID museumId, String accountId);

    boolean existsByMuseumIdAndVisitDateAndUserId(UUID museumId, LocalDate visitDate, String userId);
}
