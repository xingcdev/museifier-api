package com.xingcdev.museum.specifications;

import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.domain.entities.Visit;
import jakarta.persistence.criteria.*;
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

        if (searchCriteria.getKey().equalsIgnoreCase("visitUserId")) {
            Join<Museum, Visit> visitJoin = root.join("visits", JoinType.INNER);
            // select distinct
            query.distinct(true);
            return builder.equal(builder.lower(visitJoin.get("userId")), searchCriteria.getValue());
        }

        return builder.like(
                builder.lower(root.get(searchCriteria.getKey())),
                "%" + searchCriteria.getValue().toLowerCase() + "%");
    }
}
