package com.springboot.app.repo;

import com.springboot.app.dto.KeyProjection;
import com.springboot.app.entity.CustomerKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface KeyRepo extends JpaRepository<CustomerKey, Long>, JpaSpecificationExecutor<CustomerKey> {
    @Query(value = "SELECT c.type as type, COUNT(c.id) as count FROM customer_key c WHERE c.cid = :cid AND " +
            "c.key_proxy in (:keys) and c.outdated > :time group by c.type", nativeQuery = true)
    List<KeyProjection> findByKeysAndCustomerId(@Param("keys") Set<String> keys, @Param("cid") Long customerId, @Param("time") Instant time);

    @Query(value = """
            UPDATE customer_key
                        SET alias = :alias, outdated = :outdated, updated_at = now()
            WHERE id in (:ids)
            """, nativeQuery = true)
    void updateCustomerKey(@Param("ids") Set<Long> ids, @Param("alias") String alias, @Param("outdated") Instant outdated);

    Set<CustomerKey> findCustomerKeyByIdInAndCustomerId(@Param("ids") Set<Long> ids, @Param("cid") Long cid);

    List<CustomerKey> findAllByCustomerIdAndKeyProxyIn(Long customerId, Collection<String> keyProxy);
}
