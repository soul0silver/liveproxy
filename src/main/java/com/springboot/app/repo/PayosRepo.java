package com.springboot.app.repo;

import com.springboot.app.entity.PayosAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayosRepo extends JpaRepository<PayosAcc, Integer> {
    Optional<PayosAcc> findPayosAccByEmail(String email);
}
