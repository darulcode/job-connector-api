package com.enigma.jobConnector.specification;

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

            // Filter berdasarkan status
            if (StringUtils.hasText(request.getStatus())) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), request.getStatus());
                predicates.add(statusPredicate);
            }

            // Filter berdasarkan admin
            if (StringUtils.hasText(request.getAdmin())) {
                // Admin diasumsikan ada di kolom `user.name` atau properti yang sesuai
                Predicate adminPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("name")),
                        "%" + request.getAdmin().toLowerCase() + "%");
                predicates.add(adminPredicate);
            }

            // Filter berdasarkan client
            if (StringUtils.hasText(request.getClient())) {
                Predicate clientPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("name")),
                        "%" + request.getClient().toLowerCase() + "%");
                predicates.add(clientPredicate);
            }

            // Filter umum (query)
            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getQuery().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + request.getQuery().toLowerCase() + "%")
                );
                predicates.add(queryPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

