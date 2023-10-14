package com.ddang.ddang.authentication.application;

import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationUserService {

    private final UserRepository userRepository;

    public boolean isWithdrawal(final Long userId) {
        return userRepository.existsByIdAndDeletedIsTrue(userId);
    }
}
