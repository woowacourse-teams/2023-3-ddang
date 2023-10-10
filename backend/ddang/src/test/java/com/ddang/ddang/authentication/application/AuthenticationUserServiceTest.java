package com.ddang.ddang.authentication.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.authentication.application.fixture.AuthenticationUserServiceFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationUserServiceTest extends AuthenticationUserServiceFixture {

    @Autowired
    AuthenticationUserService authenticationUserService;

    @Test
    void 회원탈퇴한_회원의_id를_전달하면_참을_반환한다() {
        // when
        final boolean actual = authenticationUserService.isWithdrawal(탈퇴한_사용자.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 회원탈퇴하지_않거나_회원가입하지_않은_회원의_id를_전달하면_거짓을_반환한다() {
        // when
        final boolean actual = authenticationUserService.isWithdrawal(사용자.getId());

        // then
        assertThat(actual).isFalse();
    }
}
