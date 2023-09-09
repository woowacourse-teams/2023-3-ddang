package com.ddang.ddang.authentication.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.Image;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationUserServiceTest {

    @Autowired
    JpaUserRepository userRepository;


    @Autowired
    AuthenticationUserService authenticationUserService;

    @Test
    void 회원탈퇴한_회원의_id를_전달하면_참을_반환한다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage(new Image("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        user.withdrawal();
        userRepository.save(user);

        // when
        final boolean actual = authenticationUserService.isWithdrawal(user.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 회원탈퇴하지_않거나_회원가입하지_않은_회원의_id를_전달하면_거짓을_반환한다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage(new Image("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        // when
        final boolean actual = authenticationUserService.isWithdrawal(user.getId());

        // then
        assertThat(actual).isFalse();
    }
}
