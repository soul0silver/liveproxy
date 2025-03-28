package com.springboot.app.repo;

import com.springboot.app.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByOrderCode(Long orderCode);

    Optional<Order> findFirstByStatusAndCid(String status, Long cid);

    List<Order> findAllByStatus(String status,  Pageable pageable);
}
