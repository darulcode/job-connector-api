package com.enigma.jobConnector.specification;

import com.enigma.jobConnector.dto.request.ClientSearchRequest;
import com.enigma.jobConnector.entity.Client;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClientSpecification {

    public static Specification<Client> getSpecification(ClientSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            log.info("Create user Specification");
            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery() + "%");
                predicates.add(queryPredicate);
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
