package com.xingcdev.museum.services;

import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import com.xingcdev.museum.repositories.VisitRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final MuseumService museumService;

    public VisitService(VisitRepository visitRepository, MuseumService museumService) {
        this.visitRepository = visitRepository;
        this.museumService = museumService;
    }

    public CustomPage<Visit> findAllByUserId(String userId, int page, int pageSize) {
        var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize);
        return new CustomPage<>(visitRepository.findByUserId(userId, pageRequest));
    }

    public Optional<Visit> findOneByUserId(UUID visitId, String userId) {
        return visitRepository.findByIdAndUserId(visitId, userId);
    }

    public Visit save(Visit visit) {
        return visitRepository.save(visit);
    }

    public Visit partialUpdate(UUID id, Visit newVisit) {
        // Make sure the id is the one from the param
        newVisit.setId(id);
        // 1. Retrieve the existing visit
        // 2. Update its attributes
        return visitRepository.findById(id).map(existingVisit -> {
            Optional.ofNullable(newVisit.getTitle()).ifPresent(existingVisit::setTitle);
            Optional.ofNullable(newVisit.getVisitDate()).ifPresent(existingVisit::setVisitDate);
            Optional.ofNullable(newVisit.getComment()).ifPresent(existingVisit::setComment);
            Optional.of(newVisit.getRating()).ifPresent(existingVisit::setRating);

            Optional.ofNullable(newVisit.getMuseum()).ifPresent(museum -> {
                var museumId = museum.getId();
                museumService.findOne(museumId).ifPresentOrElse(existingVisit::setMuseum, () -> {
                    throw new MuseumNotFoundException(museumId.toString());
                });
            });

            return visitRepository.save(existingVisit);

        }).orElseThrow(() -> new VisitNotFoundException(id));
    }

    public boolean existsByUserId(UUID id, String userId) {
        return visitRepository.existsByIdAndUserId(id, userId);
    }

    public boolean existsByMuseumIdAndVisitDate(UUID museumId, LocalDate visitDate, String userId) {
        return visitRepository.existsByMuseumIdAndVisitDateAndUserId(museumId, visitDate, userId);
    }

    public boolean isMuseumAlreadyVisited(UUID museumId, String userId) {
        return visitRepository.existsByMuseumIdAndUserId(museumId, userId);
    }

    public void delete(UUID id) {
        visitRepository.deleteById(id);
    }

}
