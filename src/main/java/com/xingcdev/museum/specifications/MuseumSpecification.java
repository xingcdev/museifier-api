package com.xingcdev.museum.specifications;

import com.xingcdev.museum.domain.entities.Museum;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class MuseumSpecification implements Specification<Museum> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Museum> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // If key == "q", do 'like' on museum name.
        if (searchCriteria.getKey().equalsIgnoreCase("q")) {
            return builder.like(
                    builder.lower(root.get("name")),
                    "%" + searchCriteria.getValue().toLowerCase() + "%");
        }

        return builder.like(
                builder.lower(root.get(searchCriteria.getKey())),
                "%" + searchCriteria.getValue().toLowerCase() + "%");
    }
}
