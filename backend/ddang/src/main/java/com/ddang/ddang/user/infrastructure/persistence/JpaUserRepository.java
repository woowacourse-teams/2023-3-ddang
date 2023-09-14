package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthIdAndDeletedIsFalse(final String oauthId);

    Optional<User> findByIdAndDeletedIsFalse(final Long id);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByNameEndingWith(final String name);
}
