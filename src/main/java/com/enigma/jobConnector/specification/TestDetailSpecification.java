package com.enigma.jobConnector.specification;

import com.enigma.jobConnector.constants.EntityStatus;
import com.enigma.jobConnector.entity.TestDetail;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
public class TestDetailSpecification {
    public static Specification<TestDetail> getSpecification(String userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("test").get("isDeleted"), EntityStatus.AKTIVE));
            if (StringUtils.hasText(userId)) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}