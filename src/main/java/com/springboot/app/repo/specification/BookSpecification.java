package com.springboot.app.repo.specification;

import com.springboot.app.dto.BookCriteria;
import com.springboot.app.entity.Book;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookSpecification {

    public Specification<Book> getSpecification(BookCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(criteria.getTitle())) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }

            if (criteria.getAuthorId() != null) {
                predicates.add(cb.isMember(criteria.getAuthorId(), root.get("authors")));
            }

            if (criteria.getCategoryId() != null) {
                predicates.add(cb.isMember(criteria.getCategoryId(), root.get("categories")));
            }

            if (criteria.getPublisherId() != null) {
                predicates.add(cb.equal(root.get("publisher").get("id"), criteria.getPublisherId()));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(cb.ge(root.get("price"), criteria.getMinPrice()));
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.le(root.get("price"), criteria.getMaxPrice()));
            }

            if (StringUtils.isNotBlank(criteria.getStatus())) {
                predicates.add(cb.equal(root.get("status").as(String.class), criteria.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
