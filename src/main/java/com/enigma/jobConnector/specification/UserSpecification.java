package com.enigma.jobConnector.specification;

import com.enigma.jobConnector.constants.EntityStatus;
import com.enigma.jobConnector.dto.request.UserSearchRequest;
import com.enigma.jobConnector.entity.User;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserSpecification {

    public static Specification<User> getSpecification(UserSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery().toLowerCase() + "%");
                predicates.add(queryPredicate);
            }
            if (StringUtils.hasText(request.getRole())) {
                Predicate queryPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("role")), request.getRole().toLowerCase() );
                predicates.add(queryPredicate);
            }
                predicates.add(criteriaBuilder.equal(root.get("status"), EntityStatus.AKTIVE));

            predicates.add(criteriaBuilder.notEqual(root.get("role"), "ROLE_SUPER_ADMIN"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
