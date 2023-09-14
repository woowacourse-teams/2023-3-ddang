package com.ddang.ddang.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserTest {

    @Test
    void 회원_탈퇴한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .build();

        // when
        user.withdrawal();

        // then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void 탈퇴하지_않음_회원의_이름은_저장된_이름으로_반환된다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .build();

        // when
        final String actual = user.getName();

        // then
        assertThat(actual).isEqualTo("kakao12345");
    }

    @Test
    void 탈퇴한_회원의_이름은_알_수_없음으로_반환된다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .build();

        user.withdrawal();

        // when
        final String actual = user.getName();

        // then
        assertThat(actual).isEqualTo("알 수 없음");
    }
}
