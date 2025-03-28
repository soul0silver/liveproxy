package com.springboot.app.repo;

import com.springboot.app.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProxyRepo extends JpaRepository<Proxy, String>, JpaSpecificationExecutor<Proxy> {
    @Query(value = """
            select p.* from proxies p
            order by p.updated_at asc limit :limit
            """, nativeQuery = true)
    List<Proxy> getListProxies(@Param("limit") int limit);

    @Transactional
    @Modifying
    @Query(value = """
            update proxies p
            set p.updated_at = now() where p.id in (:ids)
            """, nativeQuery = true)
    void setUpdated(@Param("ids") List<String> ids);

}
