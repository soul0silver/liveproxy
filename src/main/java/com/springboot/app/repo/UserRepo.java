package com.springboot.app.repo;

import com.springboot.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query(value = """
            UPDATE users u SET u.wallet = (u.wallet - :amount) WHERE id = :id
            """, nativeQuery = true)
    void updateWallet(@Param("amount") int amount, @Param("id") Long userId);

    @Modifying
    @Query(value = """
            UPDATE users u SET u.wallet = (u.wallet + :amount) WHERE id = :id
            """, nativeQuery = true)
    void recharge(@Param("amount") int amount, @Param("id") Long userId);
}
