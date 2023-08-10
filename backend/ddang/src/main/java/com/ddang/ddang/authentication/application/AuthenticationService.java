package com.ddang.ddang.authentication.application;

import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String PRIVATE_CLAIMS_KEY = "userId";

    private final Oauth2UserInformationProviderComposite providerComposite;
    private final JpaUserRepository userRepository;
    private final TokenEncoder tokenEncoder;
    private final TokenDecoder tokenDecoder;

    @Transactional
    public TokenDto login(final Oauth2Type oauth2Type, final String oauth2AccessToken) {
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        final UserInformationDto userInformationDto = provider.findUserInformation(oauth2AccessToken);
        final User persistUser = findOrPersistUser(oauth2Type, userInformationDto);

        return convertTokenDto(persistUser);
    }

    private User findOrPersistUser(final Oauth2Type oauth2Type, final UserInformationDto userInformationDto) {
        return userRepository.findByOauthId(userInformationDto.findUserId())
                             .orElseGet(() -> {
                                 final User user = User.builder()
                                                       .name(oauth2Type.calculateNickname(userInformationDto))
                                                       .profileImage(null)
                                                       .reliability(0.0d)
                                                       .oauthId(userInformationDto.findUserId())
                                                       .build();

                                 return userRepository.save(user);
                             });
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

    @Transactional(readOnly = true)
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
}
