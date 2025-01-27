package com.xingcdev.museum.controllers;

import com.xingcdev.museum.domain.dto.GetNearbyMuseumsDto;
import com.xingcdev.museum.domain.dto.MuseumDto;
import com.xingcdev.museum.domain.dto.VisitedMuseumDto;
import com.xingcdev.museum.domain.entities.CustomPage;
import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.exceptions.InvalidCoordinatesException;
import com.xingcdev.museum.mappers.impl.MuseumDtoMapper;
import com.xingcdev.museum.mappers.impl.MuseumWithVisitsDtoMapper;
import com.xingcdev.museum.mappers.impl.NearbyMuseumDtoMapper;
import com.xingcdev.museum.mappers.impl.VisitedMuseumDtoMapper;
import com.xingcdev.museum.services.MuseumService;
import com.xingcdev.museum.specifications.MuseumSpecificationBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class MuseumController {

    private final MuseumService museumService;
    private final MuseumDtoMapper museumMapper;
    private final MuseumWithVisitsDtoMapper museumWithVisitsDtoMapper;
    private final VisitedMuseumDtoMapper visitedMuseumDtoMapper;

    private final NearbyMuseumDtoMapper nearbyMuseumDtoMapper;

    public MuseumController(MuseumService museumService, MuseumDtoMapper museumMapper, MuseumWithVisitsDtoMapper museumWithVisitsDtoMapper, VisitedMuseumDtoMapper visitedMuseumDtoMapper, NearbyMuseumDtoMapper nearbyMuseumDtoMapper) {
        this.museumService = museumService;
        this.museumMapper = museumMapper;
        this.museumWithVisitsDtoMapper = museumWithVisitsDtoMapper;
        this.visitedMuseumDtoMapper = visitedMuseumDtoMapper;
        this.nearbyMuseumDtoMapper = nearbyMuseumDtoMapper;
    }

    @GetMapping(path = "/museums")
    public CustomPage<MuseumDto> getMuseums(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int pageSize,
            @Parameter(example = "name:asc") @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "q", required = false) String searchQuery,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "department", required = false) String department

    ) {
        var museumSpecificationBuilder = new MuseumSpecificationBuilder();
        String[] sortParams = new String[0];

        if (sort != null && !sort.isEmpty()) {
            sortParams = sort.split(":");
        }

        var isFiltering = searchQuery != null || address != null || postalCode != null || city != null || department != null;

        // With filtering
        if (isFiltering) {

            if (searchQuery != null) {
                museumSpecificationBuilder.with("q", searchQuery);
            }

            if (address != null) {
                museumSpecificationBuilder.with("address", address);
            }

            if (postalCode != null) {
                museumSpecificationBuilder.with("postalCode", postalCode);
            }

            if (city != null) {
                museumSpecificationBuilder.with("city", city);
            }

            if (department != null) {
                museumSpecificationBuilder.with("department", department);
            }

            var museumSpecification = museumSpecificationBuilder.build();

            if (sortParams.length >= 2) {
                return museumMapper.mapToCustomPageDto(museumService.findAllWithFiltering(page, pageSize, museumSpecification, Optional.of(sortParams[0]), Optional.of(sortParams[1])));
            } else if (sortParams.length == 1) {
                return museumMapper.mapToCustomPageDto(museumService.findAllWithFiltering(page, pageSize, museumSpecification, Optional.of(sortParams[0]), Optional.empty()));
            } else {
                return museumMapper.mapToCustomPageDto(museumService.findAllWithFiltering(page, pageSize, museumSpecification, Optional.empty(), Optional.empty()));
            }
        }

        // Without filtering
        if (sortParams.length >= 2) {
            return museumMapper.mapToCustomPageDto(museumService.findAll(page, pageSize, Optional.of(sortParams[0]), Optional.of(sortParams[1])));
        } else if (sortParams.length == 1) {
            return museumMapper.mapToCustomPageDto(museumService.findAll(page, pageSize, Optional.of(sortParams[0]), Optional.empty()));
        } else {
            return museumMapper.mapToCustomPageDto(museumService.findAll(page, pageSize, Optional.empty(), Optional.empty()));
        }
    }

    @GetMapping(path = "/museums/visited")
    public CustomPage<VisitedMuseumDto> getVisitedMuseums(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int pageSize,
            @Parameter(example = "name:asc") @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "q", required = false) String searchQuery,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "department", required = false) String department

    ) {
        var museumSpecificationBuilder = new MuseumSpecificationBuilder();
        museumSpecificationBuilder.with("visitUserId", jwt.getSubject());
        String[] sortParams = new String[0];

        if (sort != null && !sort.isEmpty()) {
            sortParams = sort.split(":");
        }

        var isFiltering = searchQuery != null || postalCode != null || city != null || department != null;

        // With filtering
        if (isFiltering) {

            if (searchQuery != null) {
                museumSpecificationBuilder.with("q", searchQuery);
            }

            if (postalCode != null) {
                museumSpecificationBuilder.with("postalCode", postalCode);
            }

            if (city != null) {
                museumSpecificationBuilder.with("city", city);
            }

            if (department != null) {
                museumSpecificationBuilder.with("department", department);
            }

            var museumSpecification = museumSpecificationBuilder.build();

            if (sortParams.length >= 2) {
                return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisitedWithFiltering(page, pageSize, museumSpecification, Optional.of(sortParams[0]), Optional.of(sortParams[1])));
            } else if (sortParams.length == 1) {
                return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisitedWithFiltering(page, pageSize, museumSpecification, Optional.of(sortParams[0]), Optional.empty()));
            } else {
                return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisitedWithFiltering(page, pageSize, museumSpecification, Optional.empty(), Optional.empty()));
            }
        }

        // Without filtering
        if (sortParams.length >= 2) {
            return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisited(jwt.getSubject(), page, pageSize, Optional.of(sortParams[0]), Optional.of(sortParams[1])));
        } else if (sortParams.length == 1) {
            return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisited(jwt.getSubject(), page, pageSize, Optional.of(sortParams[0]), Optional.empty()));
        } else {
            return visitedMuseumDtoMapper.mapToCustomPageDto(museumService.findVisited(jwt.getSubject(), page, pageSize, Optional.empty(), Optional.empty()));
        }
    }

    @GetMapping(path = "/museums/{id}")
    public ResponseEntity<VisitedMuseumDto> getMuseum(@PathVariable("id") UUID id) {
        Optional<Museum> foundMuseum = museumService.findOne(id);
        // Convert Museum entity into MuseumDto
        return foundMuseum.map(museum -> {
            var museumDto = this.visitedMuseumDtoMapper.mapToDto(museum);
            return new ResponseEntity<>(museumDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/museums/nearby")
    public ResponseEntity<GetNearbyMuseumsDto> getNearbyMuseums(
            @RequestParam(value = "location") String location
    ) {

        try {
            var latLonList = location.split(",");
            if (latLonList.length > 2) {
                throw new InvalidCoordinatesException();
            }
            var latitude = Double.parseDouble(latLonList[0]);
            var longitude = Double.parseDouble(latLonList[1]);

            var nearbyMuseums = museumService.findNearby(latitude, longitude);
            var nearbyMuseumsDto = nearbyMuseums.stream().map(nearbyMuseumDtoMapper::mapToDto).toList();

            var dto = GetNearbyMuseumsDto.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .data(nearbyMuseumsDto)
                    .totalResults(nearbyMuseumsDto.size())
                    .build();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            throw new InvalidCoordinatesException();
        }


    }
}
