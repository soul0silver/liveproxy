package com.springboot.app.repo;

import com.springboot.app.entity.InUsePort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InUseProxyRepo extends JpaRepository<InUsePort, String> {
    @Query(value = "select * from in_use_proxy where out_date <= now() limit :i",nativeQuery = true)
    List<InUsePort> list(@Param("i") int i);
    List<InUsePort> findAllByKeyIn(List<String> keys);
}
