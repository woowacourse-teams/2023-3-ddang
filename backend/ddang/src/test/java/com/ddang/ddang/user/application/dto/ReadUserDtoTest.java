package com.ddang.ddang.user.application.dto;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class ReadUserDtoTest {

    @Test
    void dto_생성시_이미지가_있다면_해당_이미지의_아이디가_포함되어_반환된다() {
        // given
        final User user = User.builder()
                               .name("회원")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(4.7d)
                               .oauthId("12345")
                               .build();

        // when
        final ReadUserDto actual = ReadUserDto.from(user);

        // then
        assertThat(actual.profileImageId()).isEqualTo(user.getProfileImage().getId());
    }

    @Test
    void dto_생성시_이미지가_없다면_이미지가_null로_처리되어_반환된다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        // when
        final ReadUserDto actual = ReadUserDto.from(user);

        // then
        assertThat(actual.profileImageId()).isNull();
    }
}
