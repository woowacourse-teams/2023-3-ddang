package com.ddang.ddang.authentication.application;

import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.authentication.application.dto.LoginInformationDto;
import com.ddang.ddang.authentication.application.dto.LoginUserInformationDto;
import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.application.exception.WithdrawalNotAllowedException;
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
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String PRIVATE_CLAIMS_KEY = "userId";

    private final DeviceTokenService deviceTokenService;
    private final Oauth2UserInformationProviderComposite providerComposite;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final TokenEncoder tokenEncoder;
    private final TokenDecoder tokenDecoder;
    private final BlackListTokenService blackListTokenService;
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public LoginInformationDto login(
            final Oauth2Type oauth2Type,
            final String oauth2AccessToken,
            final String deviceToken
    ) {
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        final UserInformationDto userInformationDto = provider.findUserInformation(oauth2AccessToken);
        final LoginUserInformationDto loginUserInfo = findOrPersistUser(oauth2Type, userInformationDto);

        updateOrPersistDeviceToken(deviceToken, loginUserInfo.user());

        return LoginInformationDto.of(convertTokenDto(loginUserInfo), loginUserInfo);
    }

    private void updateOrPersistDeviceToken(final String deviceToken, final User persistUser) {
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(deviceToken);

        deviceTokenService.persist(persistUser.getId(), persistDeviceTokenDto);
    }

    private LoginUserInformationDto findOrPersistUser(
            final Oauth2Type oauth2Type,
            final UserInformationDto userInformationDto
    ) {
        final AtomicBoolean isSignUpUser = new AtomicBoolean(false);

        final User signInUser = userRepository.findByOauthId(userInformationDto.findUserId())
                                              .orElseGet(() -> {
                                                  final User user = User.builder()
                                                                        .name(oauth2Type.calculateNickname(
                                                                                calculateRandomNumber())
                                                                        )
                                                                        .reliability(Reliability.INITIAL_RELIABILITY)
                                                                        .oauthId(userInformationDto.findUserId())
                                                                        .oauth2Type(oauth2Type)
                                                                        .build();

                                                  isSignUpUser.set(true);
                                                  return userRepository.save(user);
                                              });

        return new LoginUserInformationDto(signInUser, isSignUpUser.get());
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

    private TokenDto convertTokenDto(final LoginUserInformationDto signInUserInfo) {
        final User loginUser = signInUserInfo.user();

        final String accessToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of(PRIVATE_CLAIMS_KEY, loginUser.getId())
        );
        final String refreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(PRIVATE_CLAIMS_KEY, loginUser.getId())
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

        final String newRefreshToken = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of(PRIVATE_CLAIMS_KEY, privateClaims.userId())
        );

        return new TokenDto(accessToken, newRefreshToken);
    }

    public boolean validateToken(final String accessToken) {
        return tokenDecoder.decode(TokenType.ACCESS, accessToken)
                           .isPresent();
    }

    @Transactional
    public void withdrawal(
            final String accessToken,
            final String refreshToken
    ) throws InvalidWithdrawalException {
        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.ACCESS, accessToken)
                                                        .orElseThrow(() -> new InvalidTokenException("유효한 토큰이 아닙니다."));
        final User user = userRepository.findById(privateClaims.userId())
                                        .orElseThrow(() -> new InvalidWithdrawalException("탈퇴에 대한 권한이 없습니다."));
        final OAuth2UserInformationProvider provider =
                providerComposite.findProvider(user.getOauthInformation().getOauth2Type());

        validateCanWithdrawal(user);

        user.withdrawal();
        blackListTokenService.registerBlackListToken(accessToken, refreshToken);
        deviceTokenRepository.deleteByUserId(user.getId());
        provider.unlinkUserBy(user.getOauthInformation().getOauthId());
    }

    private void validateCanWithdrawal(final User user) {
        final LocalDateTime now = LocalDateTime.now();

        if (auctionRepository.existsBySellerIdAndAuctionStatusIsOngoing(user.getId(), now)) {
            throw new WithdrawalNotAllowedException("등록한 경매 중 현재 진행 중인 것이 있기에 탈퇴할 수 없습니다.");
        }
        if (auctionRepository.existsLastBidByUserIdAndAuctionStatusIsOngoing(user.getId(), now)) {
            throw new WithdrawalNotAllowedException("마지막 입찰자로 등록되어 있는 것이 있기에 탈퇴할 수 없습니다.");
        }
    }
}
