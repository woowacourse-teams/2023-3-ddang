package com.ddang.ddang.authentication.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.ddang.ddang.authentication.application.dto.response.SocialUserInfoDto;
import com.ddang.ddang.authentication.application.fixture.SocialUserInformationServiceFixture;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SocialUserInformationServiceTest extends SocialUserInformationServiceFixture {

    @MockBean
    Oauth2UserInformationProviderComposite providerComposite;

    @MockBean
    OAuth2UserInformationProvider userInfoProvider;

    @Autowired
    SocialUserInformationService socialUserInformationService;

    @Test
    void 지원하는_소셜_로그인_타입으로_유효한_토큰을_전달하면_사용자의_소셜_정보를_반환한다() {
        // given
        given(providerComposite.findProvider(지원하는_소셜_로그인_타입)).willReturn(userInfoProvider);
        given(userInfoProvider.findUserInformation(anyString())).willReturn(사용자_소셜_정보);

        // when
        final SocialUserInfoDto actual = socialUserInformationService.findInformation(
                지원하는_소셜_로그인_타입,
                유효한_소셜_로그인_토큰
        );

        // then
        assertThat(actual.id()).isEqualTo(사용자_소셜_정보.findUserId());
    }

    @Test
    void 권한이_없는_소셜_로그인_토큰을_전달하면_예외가_발생한다() {
        // given
        given(providerComposite.findProvider(지원하는_소셜_로그인_타입)).willReturn(userInfoProvider);
        given(userInfoProvider.findUserInformation(anyString())).willThrow(new InvalidTokenException("401 Unauthorized"));

        // when & then
        assertThatThrownBy(
                () -> socialUserInformationService.findInformation(
                        지원하는_소셜_로그인_타입,
                        권한이_없는_소셜_로그인_토큰
                )
        ).isInstanceOf(InvalidTokenException.class)
         .hasMessage("401 Unauthorized");
    }

    @Test
    void 지원하는_소셜_로그인_기능이_아닌_경우_예외가_발생한다() {
        // given
        given(providerComposite.findProvider(지원하지_않는_소셜_로그인_타입))
                .willThrow(new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다."));

        // when & then
        assertThatThrownBy(
                () -> socialUserInformationService.findInformation(
                        지원하지_않는_소셜_로그인_타입,
                        유효한_소셜_로그인_토큰
                )
        )
                .isInstanceOf(UnsupportedSocialLoginException.class)
                .hasMessage("지원하는 소셜 로그인 기능이 아닙니다.");
    }
}
