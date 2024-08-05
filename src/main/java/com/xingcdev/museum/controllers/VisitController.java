package com.xingcdev.museum.controllers;

import com.xingcdev.museum.domain.dto.VisitRequestBody;
import com.xingcdev.museum.domain.dto.VisitDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.AccountNotFoundException;
import com.xingcdev.museum.exceptions.DuplicateMuseumException;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import com.xingcdev.museum.mappers.impl.VisitDtoMapper;
import com.xingcdev.museum.mappers.impl.VisitRequestBodyMapper;
import com.xingcdev.museum.services.AccountService;
import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.services.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class VisitController {

    private final VisitService visitService;
    private final MuseumService museumService;
    private final AccountService accountService;
    private final VisitRequestBodyMapper visitRequestBodyMapper;
    private final VisitDtoMapper visitDtoMapper;

    public VisitController(VisitService visitService, VisitRequestBodyMapper visitRequestBodyMapper, MuseumService museumService, AccountService accountService, VisitDtoMapper visitDtoMapper) {
        this.visitService = visitService;
        this.visitRequestBodyMapper = visitRequestBodyMapper;
        this.museumService = museumService;
        this.accountService = accountService;
        this.visitDtoMapper = visitDtoMapper;
    }

    @GetMapping(path = "/visits")
    public CustomPage<VisitDto> getVisits(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "20") int pageSize
    ) {
        return visitDtoMapper.mapToPageDto(visitService.findAll(page, pageSize));
    }

    @GetMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> getVisit(@PathVariable("id") UUID id) {
        var foundVisit = visitService.findOne(id);
        return foundVisit.map(visit -> {
            var visitDto = visitDtoMapper.mapToDto(visit);
            return new ResponseEntity<>(visitDto, HttpStatus.OK);
        }).orElseThrow(() -> new VisitNotFoundException(id));
    }

    @PostMapping(path = "/visits")
    public ResponseEntity<VisitDto> createVisit(@RequestBody VisitRequestBody visitRequestBody) {

        // 1. Check if the museum exists
        var foundMuseum = museumService.findOne(visitRequestBody.getMuseumId());
        if (foundMuseum.isEmpty()) throw new MuseumNotFoundException(visitRequestBody.getMuseumId().toString());

        // 2. Check if the account exists
        var foundAccount = accountService.findOne(visitRequestBody.getAccountId());
        if (foundAccount.isEmpty()) throw new AccountNotFoundException(visitRequestBody.getAccountId().toString());

        // 3. Check if the museum is already visited
        var isMuseumAlreadyExist = visitService.isMuseumAlreadyVisited(visitRequestBody.getMuseumId(), visitRequestBody.getAccountId());
        if (isMuseumAlreadyExist) throw new DuplicateMuseumException(visitRequestBody.getMuseumId().toString());

        Visit visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        visit.setMuseum(foundMuseum.get());
        visit.setAccount(foundAccount.get());
        Visit savedVisit = visitService.save(visit);
        return new ResponseEntity<>(this.visitDtoMapper.mapToDto(savedVisit), HttpStatus.CREATED);
    }

    @PutMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> fullUpdateVisit(@PathVariable("id") UUID id, @RequestBody VisitRequestBody visitRequestBody) {
        if (!visitService.isExists(id)) throw new VisitNotFoundException(id);

        // 1. Check if the museum exists
        var foundMuseum = museumService.findOne(visitRequestBody.getMuseumId());
        if (foundMuseum.isEmpty()) throw new MuseumNotFoundException(visitRequestBody.getMuseumId().toString());

        // 2. Check if the account exists
        var foundAccount = accountService.findOne(visitRequestBody.getAccountId());
        if (foundAccount.isEmpty()) throw new AccountNotFoundException(visitRequestBody.getAccountId().toString());

        // Make sure the id is the one from the param
        visitRequestBody.setId(id);
        var visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        var savedVisit = visitService.save(visit);
        return new ResponseEntity<>(visitDtoMapper.mapToDto(savedVisit), HttpStatus.OK);
    }

    @PatchMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> partialUpdateVisit(@PathVariable("id") UUID id, @RequestBody VisitRequestBody visitRequestBody) {
        if (!visitService.isExists(id)) throw new VisitNotFoundException(id);
        var visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        var updatedVisit = visitService.partialUpdate(id, visit);
        return new ResponseEntity<>(visitDtoMapper.mapToDto(updatedVisit), HttpStatus.OK);
    }

    @DeleteMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> deleteVisit(@PathVariable("id") UUID id) {
        return visitService.findOne(id).map(foundVisit -> {
            visitService.delete(id);
            var visitDto = visitDtoMapper.mapToDto(foundVisit);
            return new ResponseEntity<>(visitDto, HttpStatus.OK);
        }).orElseThrow(() -> new VisitNotFoundException(id));
    }
}
