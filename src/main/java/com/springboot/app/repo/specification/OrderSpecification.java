package com.springboot.app.repo.specification;

import com.springboot.app.dto.KeyCriteria;
import com.springboot.app.dto.OrderCriteria;
import com.springboot.app.entity.CustomerKey;
import com.springboot.app.entity.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class OrderSpecification {
    public Specification<Order> search(Long cid, OrderCriteria orderCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if ("RECHARGE".equalsIgnoreCase(orderCriteria.getType())) {
                predicates.add(cb.equal(root.get("times"), 0));
            }

            if ("PAID".equalsIgnoreCase(orderCriteria.getType())) {
                predicates.add(cb.greaterThan(root.get("times"), 0));
            }

            if (StringUtils.isNotBlank(orderCriteria.getContent())) {
                predicates.add(cb.like(cb.upper(root.get("content")), "%"+orderCriteria.getContent().toUpperCase()+"%"));
            }

            if (orderCriteria.getFrom() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), orderCriteria.getTo()));
            }
            if (orderCriteria.getTo() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), orderCriteria.getFrom()));
            }

            predicates.add(cb.notEqual(root.get("status"), "PENDING"));
            predicates.add(cb.equal(root.get("cid"), cid));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
