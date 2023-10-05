package com.ddang.ddang.authentication.application.fixture;

import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationServiceFixture {

    protected Oauth2Type 지원하는_소셜_로그인_타입 = Oauth2Type.KAKAO;
    protected Oauth2Type 지원하지_않는_소셜_로그인_타입 = Oauth2Type.KAKAO;

    protected String 유효한_소셜_로그인_토큰 = "Bearer accessToken";
    protected String 만료된_소셜_로그인_토큰;

    protected String 디바이스_토큰 = "deviceToken";

    protected User 사용자;
    protected User 탈퇴한_사용자;
    protected User 이미지가_없는_사용자;

    protected UserInformationDto 사용자_회원_정보 = new UserInformationDto(12345L);
    protected UserInformationDto 탈퇴한_사용자_회원_정보 = new UserInformationDto(54321L);
    protected UserInformationDto 가입하지_않은_사용자_회원_정보 = new UserInformationDto(-99999L);

    protected String 유효한_액세스_토큰;
    protected String 유효하지_않은_액세스_토큰 = "Bearer invalidAccessToken";
    protected String 탈퇴한_사용자_액세스_토큰;
    protected String 이미지가_없는_사용자_액세스_토큰;
    protected String 존재하지_않는_사용자_액세스_토큰;
    protected String 유효한_리프레시_토큰;
    protected String 만료된_리프레시_토큰;
    protected String 유효하지_않은_타입의_리프레시_토큰 = "invalidRefreshToken";

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private TokenEncoder tokenEncoder;

    @Autowired
    private JpaDeviceTokenRepository deviceTokenRepository;

    @BeforeEach
    void fixtureSetUp() {
        profileImageRepository.save(new ProfileImage("default_profile_image.png", "default_profile_image.png"));

        사용자 = User.builder()
                  .name("kakao12345")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(0.0d))
                  .oauthId("12345")
                  .build();

        탈퇴한_사용자 = User.builder()
                      .name("kakao12346")
                      .profileImage(new ProfileImage("upload.png", "store.png"))
                      .reliability(new Reliability(0.0d))
                      .oauthId("12346")
                      .build();

        이미지가_없는_사용자 = User.builder()
                          .name("kakao12347")
                          .profileImage(null)
                          .reliability(new Reliability(0.0d))
                          .oauthId("12347")
                          .build();

        userRepository.save(사용자);
        탈퇴한_사용자.withdrawal();
        userRepository.save(탈퇴한_사용자);

        final DeviceToken deviceToken = new DeviceToken(사용자, 디바이스_토큰);
        deviceTokenRepository.save(deviceToken);

        유효한_리프레시_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of("userId", 1L)
        );

        만료된_리프레시_토큰 = tokenEncoder.encode(
                LocalDateTime.ofInstant(Instant.parse("2023-01-01T22:21:20Z"),  ZoneId.of("UTC")),
                TokenType.REFRESH,
                Map.of("userId", 1L)
        );

        만료된_소셜_로그인_토큰 = tokenEncoder.encode(
                Instant.parse("2000-08-10T15:30:00Z").atZone(ZoneId.of("UTC")).toLocalDateTime(),
                TokenType.ACCESS,
                Map.of("userId", 1L)
        );

        유효한_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", 1L)
        );

        탈퇴한_사용자_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", 탈퇴한_사용자.getId())
        );

        이미지가_없는_사용자_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", 가입하지_않은_사용자_회원_정보.id())
        );

        존재하지_않는_사용자_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", -99999L)
        );
    }
}
