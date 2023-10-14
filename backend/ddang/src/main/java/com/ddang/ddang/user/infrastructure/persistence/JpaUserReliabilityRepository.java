package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.UserReliability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaUserReliabilityRepository extends JpaRepository<UserReliability, Long> {

    @Query("""
        SELECT ur
        FROM UserReliability  ur
        JOIN FETCH ur.user u
        WHERE u.id = :userId
    """)
    Optional<UserReliability> findByUserId(final Long userId);
}
