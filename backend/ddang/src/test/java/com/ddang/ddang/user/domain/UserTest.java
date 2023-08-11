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
}
