package com.springboot.app.repo.specification;

import com.springboot.app.dto.KeyCriteria;
import com.springboot.app.entity.CustomerKey;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KeySpecification {
    public Specification<CustomerKey> keyEquals(Long cid, KeyCriteria keyCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyCriteria.getKeyProxy())) {
                predicates.add(cb.like(cb.upper(root.get("keyProxy")), keyCriteria.getKeyProxy().toUpperCase()));
            }
            if (StringUtils.isNotBlank(keyCriteria.getAlias())) {
                predicates.add(cb.like(cb.upper(root.get("alias")), keyCriteria.getAlias().toUpperCase()));
            }
            if (keyCriteria.getActive() != null) {
                if (keyCriteria.getActive()) {
                    predicates.add(cb.greaterThan(root.get("outdated"), Instant.now()));
                }
                else {
                    predicates.add(cb.lessThan(root.get("outdated"), Instant.now()));
                }
            }
            if (StringUtils.isNotBlank(keyCriteria.getType())) {
                predicates.add(cb.equal(root.get("type"), keyCriteria.getType()));
            }
            predicates.add(cb.equal(root.get("customerId"), cid));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
