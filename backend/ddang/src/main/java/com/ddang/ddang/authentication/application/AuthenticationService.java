package com.ddang.ddang.authentication.application;

import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.authentication.application.dto.response.LoginInfoDto;
import com.ddang.ddang.authentication.application.dto.response.LoginUserInfoDto;
import com.ddang.ddang.authentication.application.dto.response.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.application.exception.WithdrawalNotAllowedException;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.UserNameGenerator;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.application.dto.request.CreateDeviceTokenDto;
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserNameGenerator generator;

    @Transactional
    public LoginInfoDto login(
            final String socialId,
            final Oauth2Type oauth2Type,
            final String deviceToken
    ) {
        final LoginUserInfoDto loginUserInfo = findOrPersistUser(socialId, oauth2Type);

        updateOrPersistDeviceToken(deviceToken, loginUserInfo.user());

        return LoginInfoDto.of(convertTokenDto(loginUserInfo), loginUserInfo);
    }

    private LoginUserInfoDto findOrPersistUser(final String socialId, final Oauth2Type oauth2Type) {
        final AtomicBoolean isSignUpUser = new AtomicBoolean(false);
        final User signInUser = userRepository.findByOauthId(socialId)
                                              .orElseGet(() -> {
                                                  isSignUpUser.set(true);
                                                  return persistUser(socialId, oauth2Type);
                                              });

        return new LoginUserInfoDto(signInUser, isSignUpUser.get());
    }

    private User persistUser(final String socialId, final Oauth2Type oauth2Type) {
        final User user = User.builder()
                              .name(oauth2Type.calculateNickname(generator.generate()))
                              .reliability(Reliability.INITIAL_RELIABILITY)
                              .oauthId(socialId)
                              .oauth2Type(oauth2Type)
                              .build();

        return userRepository.save(user);
    }

    private void updateOrPersistDeviceToken(final String deviceToken, final User persistUser) {
        final CreateDeviceTokenDto createDeviceTokenDto = new CreateDeviceTokenDto(deviceToken);

        deviceTokenService.persist(persistUser.getId(), createDeviceTokenDto);
    }

    private TokenDto convertTokenDto(final LoginUserInfoDto signInUserInfo) {
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
