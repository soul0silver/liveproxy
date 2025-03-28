package com.springboot.app.repo.specification;

import com.springboot.app.dto.ProxyRequest;
import com.springboot.app.dto.constant.ProxyStatus;
import com.springboot.app.entity.Proxy;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProxySpecification {
    public Specification<Proxy> getSpecification(ProxyRequest proxyRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("key").in(proxyRequest.getKeys()));

            if (StringUtils.isNotBlank(proxyRequest.getLocation())) {
                predicates.add(cb.equal(root.get("location"), proxyRequest.getLocation()));
            }

            predicates.add(cb.equal(root.get("status"), ProxyStatus.FREE.getValue()));
            query.orderBy(cb.asc(cb.function("RAND", null)));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
