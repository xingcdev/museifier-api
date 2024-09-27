package com.xingcdev.museum.specifications;

import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.domain.entities.Visit;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class MuseumSpecification implements Specification<Museum> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Museum> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // If key == "q", do 'like' on museum name.
        if (searchCriteria.getKey().equalsIgnoreCase("q")) {
            var unaccentedName = builder.function("unaccent", String.class, builder.lower(root.get("name")));
            var unaccentedValue = StringUtils.stripAccents(searchCriteria.getValue().toLowerCase());
            return builder.like(
                    unaccentedName,
                    "%" + unaccentedValue + "%");
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
