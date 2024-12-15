package com.enigma.jobConnector.specification;

import com.enigma.jobConnector.constants.EntityStatus;
import com.enigma.jobConnector.dto.request.TestSearchRequest;
import com.enigma.jobConnector.entity.Test;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestSpecification {
    public static Specification<Test> getSpecification(TestSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getQuery().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + request.getQuery().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("name")), "%" + request.getQuery().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("name")), "%" + request.getQuery().toLowerCase() + "%")
                );
                predicates.add(queryPredicate);
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), EntityStatus.AKTIVE));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

