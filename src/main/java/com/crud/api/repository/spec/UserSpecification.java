package com.crud.api.repository.spec;

import com.crud.api.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {
    private Specification<User> userIdIs(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), userId);
    }

    private Specification<User> userNameLike(String text) {
        return ((root, query, criteriaBuilder) -> {
            Predicate p1 = criteriaBuilder.like(root.get("firstName"), "%" + text + "%");
            Predicate p2 = criteriaBuilder.like(root.get("lastName"), "%" + text + "%");
            return criteriaBuilder.and(p1, p2);
        });
    }
}
