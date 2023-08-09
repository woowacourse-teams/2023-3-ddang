package com.ddang.ddang.user.application;

import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 특정_사용자_정보를_조회한다() {
        // given
        final User user = User.builder()
                              .name("사용자")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        // when
        final ReadUserDto actual = userService.readById(new LoginUserDto(user.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.name()).isEqualTo(user.getName());
            softAssertions.assertThat(actual.profileImage()).isEqualTo(user.getProfileImage());
            softAssertions.assertThat(actual.reliability()).isEqualTo(user.getReliability());
        });
    }

    @Test
    void 존재하지_않는_사용자_정보_조회시_예외를_반환한다() {
        // given
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> userService.readById(new LoginUserDto(invalidUserId)))
                .isInstanceOf(UserNotFoundException.class).hasMessage("사용자 정보를 사용할 수 없습니다.");
    }
}
