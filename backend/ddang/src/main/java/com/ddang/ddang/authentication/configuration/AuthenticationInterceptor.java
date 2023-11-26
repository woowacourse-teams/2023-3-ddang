package com.ddang.ddang.authentication.configuration;

import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final BlackListTokenService blackListTokenService;
    private final AuthenticationUserService authenticationUserService;
    private final TokenDecoder tokenDecoder;
    private final AuthenticationStore store;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotRequiredAuthenticate(accessToken)) {
            store.set(new AuthenticationUserInfo(null));
            return true;
        }

        validateLogoutToken(accessToken);

        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.ACCESS, accessToken)
                                                        .orElseThrow(() ->
                                                                new InvalidTokenException("유효한 토큰이 아닙니다.")
                                                        );

        if (authenticationUserService.isWithdrawal(privateClaims.userId())) {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.");
        }

        store.set(new AuthenticationUserInfo(privateClaims.userId()));
        return true;
    }

    private boolean isNotRequiredAuthenticate(final String token) {
        return token == null || token.length() == 0;
    }

    private void validateLogoutToken(final String accessToken) {
        if (blackListTokenService.existsBlackListToken(TokenType.ACCESS, accessToken)) {
            throw new InvalidTokenException("유효한 토큰이 아닙니다.");
        }
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) {
        store.remove();
    }
}
