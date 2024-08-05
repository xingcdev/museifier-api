package com.xingcdev.museum.services;

import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.AccountNotFoundException;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import com.xingcdev.museum.repositories.VisitRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final MuseumService museumService;
    private final AccountService accountService;

    public VisitService(VisitRepository visitRepository, MuseumService museumService, AccountService accountService) {
        this.visitRepository = visitRepository;
        this.museumService = museumService;
        this.accountService = accountService;
    }

    public CustomPage<Visit> findAll(int page, int pageSize) {
        var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize);
        return new CustomPage<>(visitRepository.findAll(pageRequest));
    }

    public Optional<Visit> findOne(UUID id) {
        return visitRepository.findById(id);
    }

    public CustomPage<Visit> findVisitsByAccountId(UUID id, int page, int pageSize) {
        var pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize);
        return new CustomPage<>(visitRepository.findByAccountId(id, pageRequest));
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
            Optional.ofNullable(newVisit.getComment()).ifPresent(existingVisit::setComment);

            Optional.ofNullable(newVisit.getMuseum()).ifPresent(museum -> {
                var museumId = museum.getId();
                museumService.findOne(museumId).ifPresentOrElse(existingVisit::setMuseum, () -> {
                    throw new MuseumNotFoundException(museumId.toString());
                });
            });

            Optional.ofNullable(newVisit.getAccount()).ifPresent(account -> {
                var accountId = account.getId();
                accountService.findOne(accountId).ifPresentOrElse(existingVisit::setAccount, () -> {
                    throw new AccountNotFoundException(accountId.toString());
                });
            });

            return visitRepository.save(existingVisit);

        }).orElseThrow(() -> new VisitNotFoundException(id));
    }

    public boolean isExists(UUID id) {
        return visitRepository.existsById(id);
    }

    public boolean isMuseumAlreadyVisited(UUID museumId, UUID accountId) {
        return visitRepository.existsByMuseumIdAndAccountId(museumId, accountId);
    }

    public void delete(UUID id) {
        visitRepository.deleteById(id);
    }

}
