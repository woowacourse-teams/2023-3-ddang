package com.ddang.ddang.authentication.infrastructure.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class KakaoOauth2TypeTest {

    @Test
    void 지원하는_소셜_로그인을_전달하면_해당_소셜_로그인_타입을_반환한다() {
        // given
        final String typeName = "kakao";

        // when
        final Oauth2Type actual = Oauth2Type.from(typeName);

        // then
        assertThat(actual).isEqualTo(Oauth2Type.KAKAO);
    }

    @Test
    void 지원하지_않는_소셜_로그인을_전달하면_예외가_발생한다() {
        // given
        final String invalidTypeName = "naver";

        // when & then
        assertThatThrownBy(() -> Oauth2Type.from(invalidTypeName))
                .isInstanceOf(UnsupportedSocialLoginException.class)
                .hasMessage("지원하는 소셜 로그인 기능이 아닙니다.");
    }

    @Test
    void 카카오_회원_ID를_전달하면_고유한_닉네임을_반환한다() {
        // given
        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        // when
        final String actual = Oauth2Type.KAKAO.calculateNickname(userInformationDto);

        // then
        assertThat(actual).contains("kakao");
    }
}
