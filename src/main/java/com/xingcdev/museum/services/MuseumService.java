package com.xingcdev.museum.services;

import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.InvalidSortingException;
import com.xingcdev.museum.repositories.MuseumRepository;
import com.xingcdev.museum.specifications.MuseumSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class MuseumService {

    private final MuseumRepository museumRepository;

    public MuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    public CustomPage<Museum> findAll(int page, int pageSize, Optional<String> sortBy, Optional<String> orderBy) {
        var sortValue = sortBy.orElse("name");
        var orderValue = orderBy.orElse("asc");

        try {
            Sort sort = Sort.by(sortValue).ascending();
            if (Objects.equals(orderValue, "desc")) {
                sort = Sort.by(sortValue).descending();
            }

            var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
            return new CustomPage<>(museumRepository.findAll(pageRequest));

        } catch(PropertyReferenceException e) {
            throw new InvalidSortingException(sortValue);
        }
    }

    public CustomPage<Museum> findAllWithFiltering(int page, int pageSize, Specification<Museum> museumSpecification, Optional<String> sortBy, Optional<String> orderBy) {
        var sortValue = sortBy.orElse("name");
        var orderValue = orderBy.orElse("asc");

        try {
            Sort sort = Sort.by(sortValue).ascending();
            if (Objects.equals(orderValue, "desc")) {
                sort = Sort.by(sortValue).descending();
            }

            var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
            return new CustomPage<>(museumRepository.findAll(museumSpecification, pageRequest));

        } catch(PropertyReferenceException e) {
            throw new InvalidSortingException(sortValue);
        }
    }

    public Optional<Museum> findOne(UUID id) {
        return museumRepository.findById(id);
    }

    public Museum save(Museum museum) {
        return museumRepository.save(museum);
    }
}
