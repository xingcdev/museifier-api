package com.xingcdev.museum.services;

import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.domain.entities.NearbyMuseum;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.InvalidSortingException;
import com.xingcdev.museum.repositories.MuseumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MuseumService {

    private final MuseumRepository museumRepository;

    private final ModelMapper mapper;

    private final GeocodingService geocodingService;

    public MuseumService(MuseumRepository museumRepository, ModelMapper mapper, GeocodingService geocodingService) {
        this.museumRepository = museumRepository;
        this.mapper = mapper;
        this.geocodingService = geocodingService;
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

        } catch (PropertyReferenceException e) {
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

        } catch (PropertyReferenceException e) {
            throw new InvalidSortingException(sortValue);
        }
    }

    public CustomPage<Museum> findVisited(String userId, int page, int pageSize, Optional<String> sortBy, Optional<String> orderBy) {
        var sortValue = sortBy.orElse("name");
        var orderValue = orderBy.orElse("asc");

        try {
            Sort sort = Sort.by(sortValue).ascending();
            if (Objects.equals(orderValue, "desc")) {
                sort = Sort.by(sortValue).descending();
            }

            var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
            return new CustomPage<>(museumRepository.findDistinctByVisitsUserId(userId, pageRequest));

        } catch (PropertyReferenceException e) {
            throw new InvalidSortingException(sortValue);
        }
    }

    public CustomPage<Museum> findVisitedWithFiltering(int page, int pageSize, Specification<Museum> museumSpecification, Optional<String> sortBy, Optional<String> orderBy) {
        var sortValue = sortBy.orElse("name");
        var orderValue = orderBy.orElse("asc");

        try {
            Sort sort = Sort.by(sortValue).ascending();
            if (Objects.equals(orderValue, "desc")) {
                sort = Sort.by(sortValue).descending();
            }

            var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
            return new CustomPage<>(museumRepository.findAll(museumSpecification, pageRequest));

        } catch (PropertyReferenceException e) {
            throw new InvalidSortingException(sortValue);
        }
    }

    public Optional<Museum> findOne(UUID id) {
        return museumRepository.findById(id);
    }

    public Museum save(Museum museum) {
        return museumRepository.save(museum);
    }

    public boolean existsById(UUID id) {
        return museumRepository.existsById(id);
    }

    public List<NearbyMuseum> findNearby(double latitude, double longitude) {
        var RADIUS_IN_KM = 4;

        List<NearbyMuseum> nearbyMuseums = new ArrayList<>();
        var museums = museumRepository.findAll();
        for (Museum museum : museums) {
            var distance = geocodingService.computeDistanceBetween(latitude, longitude, museum.getLatitude(), museum.getLongitude());
            if (distance <= RADIUS_IN_KM) {
                var nearbyMuseum = mapper.map(museum, NearbyMuseum.class);
                nearbyMuseum.setTotalVisits(museum.getVisits().size());
                nearbyMuseum.setDistance(distance);

                if (!museum.getVisits().isEmpty()) {
                    int totalRating = 0;
                    for (Visit visit : museum.getVisits()) {
                        totalRating = totalRating + visit.getRating();
                    }
                    nearbyMuseum.setAverageRating(totalRating / museum.getVisits().size());
                }


                nearbyMuseums.add(nearbyMuseum);
            }
        }

        // Sort by distance
        nearbyMuseums.sort(Comparator.comparingDouble(NearbyMuseum::getDistance));
        return nearbyMuseums;
    }
}
