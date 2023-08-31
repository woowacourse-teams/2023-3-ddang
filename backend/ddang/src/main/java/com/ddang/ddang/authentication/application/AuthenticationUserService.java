package com.ddang.ddang.authentication.application;

import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationUserService {

    private final JpaUserRepository userRepository;

    public boolean isWithdrawal(final Long userId) {
        return userRepository.existsByIdAndDeletedIsTrue(userId);
    }
}
