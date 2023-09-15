package com.ddang.ddang.authentication.application;

import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.infrastructure.jwt.JwtEncoder;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationServiceTest {

    AuthenticationService authenticationService;

    OAuth2UserInformationProvider mockProvider;

    Oauth2UserInformationProviderComposite mockProviderComposite;

    @MockBean
    DeviceTokenService deviceTokenService;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaDeviceTokenRepository userDeviceTokenRepository;

    @Autowired
    TokenEncoder tokenEncoder;

    @Autowired
    TokenDecoder tokenDecoder;

    @Autowired
    BlackListTokenService mockBlackListTokenService;

    @Autowired
    JwtEncoder jwtEncoder;

    @Mock
    Clock clock;

    @BeforeEach
    void setUp(
            @Autowired JpaUserRepository userRepository,
            @Autowired TokenEncoder tokenEncoder,
            @Autowired TokenDecoder tokenDecoder
    ) {
        mockProvider = mock(OAuth2UserInformationProvider.class);
        mockProviderComposite = mock(Oauth2UserInformationProviderComposite.class);
        mockBlackListTokenService = mock(BlackListTokenService.class);
        authenticationService = new AuthenticationService(
                deviceTokenService,
                mockProviderComposite,
                userRepository,
                tokenEncoder,
                tokenDecoder,
                mockBlackListTokenService
        );

        doNothing().when(deviceTokenService).persist(anyLong(), any(PersistDeviceTokenDto.class));
    }

    @Test
    void 지원하는_소셜_로그인_기능이_아닌_경우_예외가_발생한다() {
        // given
        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO))
                .willThrow(new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다."));

        // when & then
        assertThatThrownBy(() -> authenticationService.login(Oauth2Type.KAKAO, "accessToken", "deviceToken"))
                .isInstanceOf(UnsupportedSocialLoginException.class)
                .hasMessage("지원하는 소셜 로그인 기능이 아닙니다.");
    }

    @Test
    void 권한이_없는_소셜_로그인_토큰을_전달하면_예외가_발생한다() {
        // given
        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString()))
                .willThrow(new InvalidTokenException("401 Unauthorized"));

        final String invalidAccessToken = "invalidAccessToken";

        // when & then
        assertThatThrownBy(() -> authenticationService.login(Oauth2Type.KAKAO, invalidAccessToken, "deviceToken"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("401 Unauthorized");
    }

    @Test
    void 가입한_회원이_소셜_로그인을_할_경우_accessToken과_refreshToken을_반환한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(0.0d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString())).willReturn(userInformationDto);

        // when
        final TokenDto actual = authenticationService.login(Oauth2Type.KAKAO, "accessToken", "deviceToken");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 가입하지_않은_회원이_소셜_로그인을_할_경우_accessToken과_refreshToken을_반환한다() {
        // given
        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString())).willReturn(userInformationDto);

        // when
        final TokenDto actual = authenticationService.login(Oauth2Type.KAKAO, "accessToken", "deviceToken");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 탈퇴한_회원이_소셜_로그인을_할_경우_accessToken과_refreshToken을_반환한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(0.0d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);
        user.withdrawal();

        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString())).willReturn(userInformationDto);

        // when
        final TokenDto actual = authenticationService.login(Oauth2Type.KAKAO, "accessToken", "deviceToken");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void refreshToken을_전달하면_새로운_accessToken을_반환한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String refreshToken = "Bearer " + tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                privateClaims
        );

        // when
        final TokenDto actual = authenticationService.refreshToken(refreshToken);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.accessToken()).isNotEmpty();
            softAssertions.assertThat(actual.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 만료된_refreshToken으로_새로운_accessToken을_요청하면_예외가_발생한다() {
        // given
        final Instant instant = Instant.parse("2023-01-01T22:21:20Z");
        final ZoneId zoneId = ZoneId.of("UTC");

        given(clock.instant()).willReturn(instant);
        given(clock.getZone()).willReturn(zoneId);

        final LocalDateTime targetTime = LocalDateTime.ofInstant(instant, zoneId);

        final Map<String, Object> privateClaims = Map.of("userId", "12345");
        final String refreshToken = "Bearer " + tokenEncoder.encode(
                targetTime,
                TokenType.REFRESH,
                privateClaims
        );

        // when & then
        assertThatThrownBy(() -> authenticationService.refreshToken(refreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("유효한 토큰이 아닙니다.");
    }

    @Test
    void 유효한_토큰_타입이_아닌_refreshToken으로_새로운_accessToken을_요청하면_예외가_발생한다() {
        // given
        final String invalidRefreshToken = "invalidRefreshToken";

        // when & then
        assertThatThrownBy(() -> authenticationService.refreshToken(invalidRefreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Bearer 타입이 아닙니다.");
    }

    @Test
    void 유효한_accessToken을_검증하면_참을_반환한다() {
        // given
        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String accessToken = "Bearer " + tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                privateClaims
        );

        // when
        final boolean actual = authenticationService.validateToken(accessToken);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 만료된_accessToken을_검증하면_거짓을_반환한다() {
        // given
        final Instant instant = Instant.parse("2000-08-10T15:30:00Z");
        final LocalDateTime expiredPublishTime = instant.atZone(ZoneId.of("UTC")).toLocalDateTime();

        final Map<String, Object> privateClaims = Map.of("userId", 1L);
        final String accessToken = "Bearer " + tokenEncoder.encode(
                expiredPublishTime,
                TokenType.ACCESS,
                privateClaims
        );

        // when
        final boolean actual = authenticationService.validateToken(accessToken);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 가입한_회원이_탈퇴하는_경우_정상처리한다() throws InvalidWithdrawalException {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(0.0d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        final Map<String, Object> privateClaims = Map.of("userId", user.getId());
        final String refreshToken = "Bearer " + tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                privateClaims
        );

        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString())).willReturn(userInformationDto);
        given(mockProvider.unlinkUserBy(anyString(), anyString())).willReturn(userInformationDto);
        willDoNothing().given(mockBlackListTokenService).registerBlackListToken(anyString(), anyString());

        // when
        authenticationService.withdrawal(Oauth2Type.KAKAO, "accessToken", refreshToken);

        // then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void 이미_탈퇴한_회원이_탈퇴하는_경우_예외가_발생한다() {
        // given
        final User user = User.builder()
                              .name("kakao12345")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(0.0d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        final Map<String, Object> privateClaims = Map.of("userId", user.getId());
        final String refreshToken = "Bearer " + tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                privateClaims
        );

        user.withdrawal();

        final UserInformationDto userInformationDto = new UserInformationDto(12345L);

        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString())).willReturn(userInformationDto);
        given(mockProvider.unlinkUserBy(anyString(), anyString())).willReturn(userInformationDto);

        // when && then
        assertThatThrownBy(() -> authenticationService.withdrawal(Oauth2Type.KAKAO, "accessToken", refreshToken))
                .isInstanceOf(InvalidWithdrawalException.class)
                .hasMessage("탈퇴에 대한 권한 없습니다.");
    }

    @Test
    void 존재하지_않는_회원이_탈퇴하는_경우_예외가_발생한다() {
        // given
        given(mockProviderComposite.findProvider(Oauth2Type.KAKAO)).willReturn(mockProvider);
        given(mockProvider.findUserInformation(anyString()))
                .willThrow(new InvalidTokenException("401 Unauthorized"));

        final String invalidAccessToken = "invalidAccessToken";
        final String invalidRefreshToken = "invalidRefreshToken";

        // when & then
        assertThatThrownBy(() -> authenticationService.withdrawal(Oauth2Type.KAKAO, invalidAccessToken, invalidRefreshToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("401 Unauthorized");
    }
}
