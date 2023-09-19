package com.ddang.ddang.authentication.application;

import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.application.util.RandomNameGenerator;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String PRIVATE_CLAIMS_KEY = "userId";
    private static final String DEFAULT_PROFILE_IMAGE_STORE_NAME = "default_profile_image.png";

    private final DeviceTokenService deviceTokenService;
    private final Oauth2UserInformationProviderComposite providerComposite;
    private final JpaUserRepository userRepository;
    private final JpaProfileImageRepository profileImageRepository;
    private final TokenEncoder tokenEncoder;
    private final TokenDecoder tokenDecoder;
    private final BlackListTokenService blackListTokenService;

    @Transactional
    public TokenDto login(final Oauth2Type oauth2Type, final String oauth2AccessToken, final String deviceToken) {
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        final UserInformationDto userInformationDto = provider.findUserInformation(oauth2AccessToken);
        final User persistUser = findOrPersistUser(oauth2Type, userInformationDto);

        updateOrPersistDeviceToken(deviceToken, persistUser);

        return convertTokenDto(persistUser);
    }

    private void updateOrPersistDeviceToken(final String deviceToken, final User persistUser) {
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(deviceToken);
        deviceTokenService.persist(persistUser.getId(), persistDeviceTokenDto);
    }

    private User findOrPersistUser(final Oauth2Type oauth2Type, final UserInformationDto userInformationDto) {
        return userRepository.findByOauthIdAndDeletedIsFalse(userInformationDto.findUserId())
                             .orElseGet(() -> {
                                 final User user = User.builder()
                                                       .name(oauth2Type.calculateNickname(calculateRandomNumber()))
                                                       .profileImage(findDefaultProfileImage())
                                                       .reliability(0.0d)
                                                       .oauthId(userInformationDto.findUserId())
                                                       .build();

                                 return userRepository.save(user);
                             });
    }

    private ProfileImage findDefaultProfileImage() {
        return profileImageRepository.findByStoreName(DEFAULT_PROFILE_IMAGE_STORE_NAME)
                                     .orElseThrow(() -> new ImageNotFoundException("기본 이미지를 찾을 수 없습니다."));
    }

    private String calculateRandomNumber() {
        String name = RandomNameGenerator.generate();

        while (isAlreadyExist(name)) {
            name = RandomNameGenerator.generate();
        }

        return name;
    }

    private boolean isAlreadyExist(final String name) {
        return userRepository.existsByNameEndingWith(name);
    }

    private TokenDto convertTokenDto(final User persistUser) {
        final String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(PRIVATE_CLAIMS_KEY, persistUser.getId())
        );
        final String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(PRIVATE_CLAIMS_KEY, persistUser.getId())
        );

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto refreshToken(final String refreshToken) {
        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.REFRESH, refreshToken)
                                                        .orElseThrow(
                                                                () -> new InvalidTokenException("유효한 토큰이 아닙니다.")
                                                        );
        final String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(PRIVATE_CLAIMS_KEY, privateClaims.userId())
        );

        return new TokenDto(accessToken, refreshToken);
    }

    public boolean validateToken(final String accessToken) {
        return tokenDecoder.decode(TokenType.ACCESS, accessToken)
                           .isPresent();
    }

    @Transactional
    public void withdrawal(
            final Oauth2Type oauth2Type,
            final String oauth2AccessToken,
            final String refreshToken
    ) throws InvalidWithdrawalException {
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        final UserInformationDto userInformationDto = provider.findUserInformation(oauth2AccessToken);
        final User user = userRepository.findByOauthIdAndDeletedIsFalse(userInformationDto.findUserId())
                                        .orElseThrow(() -> new InvalidWithdrawalException("탈퇴에 대한 권한 없습니다."));

        user.withdrawal();
        blackListTokenService.registerBlackListToken(oauth2AccessToken, refreshToken);
        provider.unlinkUserBy(oauth2AccessToken, user.getOauthId());
    }
}
