package com.xingcdev.museum.repositories;

import com.xingcdev.museum.domain.entities.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends CrudRepository<Visit, UUID>, PagingAndSortingRepository<Visit, UUID> {
    Page<Visit> findByAccountId(UUID id, Pageable pageable);

    boolean existsByMuseumIdAndAccountId(UUID museumId, UUID accountId);
}
