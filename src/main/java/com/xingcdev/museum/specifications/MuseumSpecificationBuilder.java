package com.xingcdev.museum.specifications;

import com.xingcdev.museum.domain.entities.Museum;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MuseumSpecificationBuilder {
    private final List<SearchCriteria> params;

    public MuseumSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public MuseumSpecificationBuilder with(String key, String value) {
        params.add(new SearchCriteria(key, value));
        return this;
    }

    public Specification<Museum> build() {
        if (params.isEmpty()) return null;

        Specification<Museum> result = new MuseumSpecification(params.get(0));

        for (SearchCriteria searchCriteria : params) {
            result = Specification.where(result).and(new MuseumSpecification(searchCriteria));
        }

        return result;
    }
}
