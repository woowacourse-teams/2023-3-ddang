package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u
        FROM User u
        WHERE u.deleted = false AND u.oauthInformation.oauthId = :oauthId
    """)
    Optional<User> findByOauthId(final String oauthId);

    Optional<User> findByIdAndDeletedIsFalse(final Long id);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByNameEndingWith(final String name);
}
