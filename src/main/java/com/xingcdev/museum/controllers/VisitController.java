package com.xingcdev.museum.controllers;

import com.xingcdev.museum.domain.dto.VisitRequestBody;
import com.xingcdev.museum.domain.dto.VisitDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.DuplicateMuseumException;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import com.xingcdev.museum.mappers.impl.VisitDtoMapper;
import com.xingcdev.museum.mappers.impl.VisitRequestBodyMapper;
import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.services.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class VisitController {

    private final VisitService visitService;
    private final MuseumService museumService;
    private final VisitRequestBodyMapper visitRequestBodyMapper;
    private final VisitDtoMapper visitDtoMapper;

    public VisitController(VisitService visitService, VisitRequestBodyMapper visitRequestBodyMapper, MuseumService museumService, VisitDtoMapper visitDtoMapper) {
        this.visitService = visitService;
        this.visitRequestBodyMapper = visitRequestBodyMapper;
        this.museumService = museumService;
        this.visitDtoMapper = visitDtoMapper;
    }

    @GetMapping(path = "/visits")
    public CustomPage<VisitDto> getVisits(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int pageSize
    ) {
        var userId = jwt.getSubject();
        return visitDtoMapper.mapToPageDto(visitService.findAllByUserId(userId, page, pageSize));
    }

    @GetMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> getVisit(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id
    ) {
        var foundVisit = visitService.findOneByUserId(id, jwt.getSubject());
        return foundVisit.map(visit -> {
            var visitDto = visitDtoMapper.mapToDto(visit);
            return new ResponseEntity<>(visitDto, HttpStatus.OK);
        }).orElseThrow(() -> new VisitNotFoundException(id));
    }

    @PostMapping(path = "/visits")
    public ResponseEntity<VisitDto> createVisit(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody VisitRequestBody visitRequestBody) {

        // 1. Check if the museum exists
        var foundMuseum = museumService
                .findOne(visitRequestBody.getMuseumId())
                .orElseThrow(() -> new MuseumNotFoundException(visitRequestBody.getMuseumId().toString()));

        // 3. Check if the museum is already visited
        var isMuseumAlreadyExist = visitService.isMuseumAlreadyVisited(visitRequestBody.getMuseumId(), jwt.getSubject());
        if (isMuseumAlreadyExist) throw new DuplicateMuseumException(visitRequestBody.getMuseumId().toString());

        Visit visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        visit.setMuseum(foundMuseum);
        visit.setUserId(jwt.getSubject());
        Visit savedVisit = visitService.save(visit);
        return new ResponseEntity<>(this.visitDtoMapper.mapToDto(savedVisit), HttpStatus.CREATED);
    }

    @PutMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> fullUpdateVisit(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id,
            @RequestBody VisitRequestBody visitRequestBody
    ){
        if (!visitService.existsByUserId(id, jwt.getSubject())) throw new VisitNotFoundException(id);

        // 1. Check if the museum exists
        museumService
                .findOne(visitRequestBody.getMuseumId())
                .orElseThrow(() -> new MuseumNotFoundException(visitRequestBody.getMuseumId().toString()));

        // Make sure the id is the one from the param
        visitRequestBody.setId(id);
        var visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        visit.setUserId(jwt.getSubject());
        var savedVisit = visitService.save(visit);
        return new ResponseEntity<>(visitDtoMapper.mapToDto(savedVisit), HttpStatus.OK);
    }

    @PatchMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> partialUpdateVisit(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id,
            @RequestBody VisitRequestBody visitRequestBody
    ) {
        if (!visitService.existsByUserId(id, jwt.getSubject())) throw new VisitNotFoundException(id);

        var visit = visitRequestBodyMapper.mapFromDto(visitRequestBody);
        visit.setUserId(jwt.getSubject());
        var updatedVisit = visitService.partialUpdate(id, visit);
        return new ResponseEntity<>(visitDtoMapper.mapToDto(updatedVisit), HttpStatus.OK);
    }

    @DeleteMapping(path = "/visits/{id}")
    public ResponseEntity<VisitDto> deleteVisit(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") UUID id
    ) {
        return visitService.findOneByUserId(id, jwt.getSubject()).map(foundVisit -> {
            visitService.delete(id);
            var visitDto = visitDtoMapper.mapToDto(foundVisit);
            return new ResponseEntity<>(visitDto, HttpStatus.OK);
        }).orElseThrow(() -> new VisitNotFoundException(id));
    }
}
