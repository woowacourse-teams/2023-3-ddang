package com.ddang.ddang.user.domain.repository;

import com.ddang.ddang.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(final User user);

    Optional<User> findById(final Long id);

    boolean existsById(final Long id);

    Optional<User> findByOauthId(final String oauthId);

    Optional<User> findByIdAndDeletedIsFalse(final Long id);

    boolean existsByIdAndDeletedIsTrue(final Long id);

    boolean existsByNameEndingWith(final String name);
}
