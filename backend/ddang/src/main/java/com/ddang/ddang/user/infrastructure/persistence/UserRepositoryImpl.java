package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(final User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public User getByIdOrThrow(final Long id) {
        return jpaUserRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("지정한 회원을 찾지 못했습니다."));
    }

    @Override
    public Optional<User> findById(final Long id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByOauthId(final String oauthId) {
        return jpaUserRepository.findByOauthId(oauthId);
    }

    @Override
    public boolean existsByIdAndDeletedIsTrue(final Long id) {
        return jpaUserRepository.existsByIdAndDeletedIsTrue(id);
    }

    @Override
    public boolean existsByNameEndingWith(final String name) {
        return jpaUserRepository.existsByNameEndingWith(name);
    }

    @Override
    public boolean existsByName(final String name) {
        return jpaUserRepository.existsByName(name);
    }
}
