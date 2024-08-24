package com.xingcdev.museum.repositories;

import com.xingcdev.museum.domain.entities.Museum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MuseumRepository extends CrudRepository<Museum, UUID>, PagingAndSortingRepository<Museum, UUID>, JpaSpecificationExecutor<Museum> {

    //    @Query("select distinct m from Museum m join Visit v on v.museum = m.id where v.userId = ?1")
    Page<Museum> findDistinctByVisitsUserId(String userId, Pageable pageable);
}
