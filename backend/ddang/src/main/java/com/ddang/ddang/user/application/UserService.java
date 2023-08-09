package com.ddang.ddang.user.application;

import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository userRepository;

    public ReadUserDto readById(final LoginUserDto userDto) {
        final User user = userRepository.findById(userDto.usedId())
                                        .orElseThrow(() -> new UserNotFoundException("사용자 정보를 사용할 수 없습니다."));

        return ReadUserDto.from(user);
    }
}
