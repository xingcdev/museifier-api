package com.xingcdev.museum.controllers;

import com.xingcdev.museum.domain.dto.VisitDto;
import com.xingcdev.museum.domain.dto.VisitRequestBody;
import com.xingcdev.museum.domain.dto.VisitUpdateRequestBody;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Visit;
import com.xingcdev.museum.exceptions.MuseumAlreadyVisitedException;
import com.xingcdev.museum.exceptions.MuseumNotFoundException;
import com.xingcdev.museum.exceptions.VisitNotFoundException;
import com.xingcdev.museum.mappers.impl.VisitDtoMapper;
import com.xingcdev.museum.mappers.impl.VisitRequestBodyMapper;
import com.xingcdev.museum.mappers.impl.VisitUpdateRequestBodyMapper;
import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.services.VisitService;
import jakarta.validation.Valid;
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
    private final VisitUpdateRequestBodyMapper visitUpdateRequestBodyMapper;
    private final VisitDtoMapper visitDtoMapper;

    public VisitController(VisitService visitService, VisitRequestBodyMapper visitRequestBodyMapper, MuseumService museumService, VisitUpdateRequestBodyMapper visitUpdateRequestBodyMapper, VisitDtoMapper visitDtoMapper) {
        this.visitService = visitService;
        this.visitRequestBodyMapper = visitRequestBodyMapper;
        this.museumService = museumService;
        this.visitUpdateRequestBodyMapper = visitUpdateRequestBodyMapper;
        this.visitDtoMapper = visitDtoMapper;
    }

    @GetMapping(path = "/visits")
    public CustomPage<VisitDto> getVisits(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int pageSize
    ) {
        var userId = jwt.getSubject();
        return visitDtoMapper.mapToCustomPageDto(visitService.findAllByUserId(userId, page, pageSize));
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
            @Valid @RequestBody VisitRequestBody visitRequestBody) {

        // 1. Check if the museum exists
        var foundMuseum = museumService
                .findOne(visitRequestBody.getMuseumId())
                .orElseThrow(() -> new MuseumNotFoundException(visitRequestBody.getMuseumId().toString()));

        // 2. Check if there is a visit of a museum in a specific day.
        // We cannot have multiple visits for a museum in a day
        // It makes more sense to have one visit a day for the same museum
        if (visitService.existsByMuseumIdAndVisitDate(visitRequestBody.getMuseumId(), visitRequestBody.getVisitDate(), jwt.getSubject())) {
            throw new MuseumAlreadyVisitedException(visitRequestBody.getMuseumId().toString());
        }


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
            @Valid @RequestBody VisitUpdateRequestBody visitRequestBody
    ) {
        var existingVisit = visitService.findOneByUserId(id, jwt.getSubject()).orElseThrow(() -> new VisitNotFoundException(id));

        var visit = visitUpdateRequestBodyMapper.mapFromDto(visitRequestBody);
        existingVisit.setTitle(visit.getTitle());
        existingVisit.setComment(visit.getComment());
        var savedVisit = visitService.save(existingVisit);
        return new ResponseEntity<>(visitDtoMapper.mapToDto(savedVisit), HttpStatus.OK);
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
