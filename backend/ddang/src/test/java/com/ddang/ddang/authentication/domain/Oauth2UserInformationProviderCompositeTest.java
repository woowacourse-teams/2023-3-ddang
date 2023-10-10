package com.ddang.ddang.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.infrastructure.oauth2.kakao.KakaoUserInformationProvider;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class Oauth2UserInformationProviderCompositeTest {

    @Test
    void 지원하는_소셜_로그인을_전달하면_해당_소셜_로그인_provider를_반환한다() {
        // given
        final KakaoUserInformationProvider provider = new KakaoUserInformationProvider(
                null,
                null
        );
        final Oauth2UserInformationProviderComposite composite =
                new Oauth2UserInformationProviderComposite(Set.of(provider));

        // when
        final OAuth2UserInformationProvider actual = composite.findProvider(Oauth2Type.KAKAO);

        // then
        assertThat(actual).isInstanceOf(KakaoUserInformationProvider.class);
    }

    @Test
    void 지원하지_않는_소셜_로그인을_전달하면_예외가_발생한다() {
        // given
        final Oauth2UserInformationProviderComposite composite = new Oauth2UserInformationProviderComposite(Set.of());

        // when & then
        assertThatThrownBy(() -> composite.findProvider(Oauth2Type.KAKAO))
                .isInstanceOf(UnsupportedSocialLoginException.class)
                .hasMessage("지원하는 소셜 로그인 기능이 아닙니다.");
    }
}
