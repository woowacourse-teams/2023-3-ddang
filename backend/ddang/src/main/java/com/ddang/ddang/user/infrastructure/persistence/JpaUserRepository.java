package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u 
        FROM User u 
        WHERE u.deleted = false AND u.id = :id
    """)
    Optional<User> findById(final Long id);
    @Query("""
        SELECT u
        FROM User u
        WHERE u.deleted = false AND u.oauthInformation.oauthId = :oauthId
    """)
    Optional<User> findByOauthId(final String oauthId);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByNameEndingWith(final String name);
}
