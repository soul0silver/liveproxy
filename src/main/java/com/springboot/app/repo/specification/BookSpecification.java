package com.springboot.app.repo.specification;

import com.springboot.app.dto.BookCriteria;
import com.springboot.app.dto.constant.BookStatus;
import com.springboot.app.entity.Author;
import com.springboot.app.entity.Book;
import com.springboot.app.entity.Category;
import com.springboot.app.entity.Publisher;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

            // tránh duplicate khi join many-to-many
            query.distinct(true);

            /*
             * title
             */
            if (StringUtils.isNotBlank(criteria.getTitle())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + criteria.getTitle().toLowerCase() + "%"
                        )
                );
            }


            /*
             * publisher
             */
            if (criteria.getPublisherId() != null) {

                Join<Book, Publisher> publisherJoin =
                        root.join("publisher", JoinType.INNER);

                predicates.add(
                        cb.equal(
                                publisherJoin.get("id"),
                                criteria.getPublisherId()
                        )
                );
            }

            /*
             * author
             */
            if (criteria.getAuthorId() != null) {

                Join<Book, Author> authorJoin =
                        root.join("authors", JoinType.INNER);

                predicates.add(
                        cb.equal(
                                authorJoin.get("id"),
                                criteria.getAuthorId()
                        )
                );
            }

            /*
             * category
             */
            if (criteria.getCategoryId() != null) {

                Join<Book, Category> categoryJoin =
                        root.join("categories", JoinType.INNER);

                predicates.add(
                        cb.equal(
                                categoryJoin.get("id"),
                                criteria.getCategoryId()
                        )
                );
            }

            /*
             * min price
             */
            if (criteria.getMinPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("price"),
                                criteria.getMinPrice()
                        )
                );
            }

            /*
             * max price
             */
            if (criteria.getMaxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                criteria.getMaxPrice()
                        )
                );
            }

            if (StringUtils.isNotBlank(criteria.getStatus())) {
                predicates.add(
                        cb.equal(
                                root.get("status"),
                                BookStatus.valueOf(criteria.getStatus())
                        )
                );
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}