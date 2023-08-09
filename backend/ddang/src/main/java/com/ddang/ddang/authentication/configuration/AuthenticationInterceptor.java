package com.ddang.ddang.authentication.configuration;

import com.ddang.ddang.authentication.domain.PrivateClaims;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
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

    private final TokenDecoder tokenDecoder;
    private final AuthenticationStore store;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotRequiredAuthenticate(token)) {
            store.set(new AuthenticationUserInfo(null));
            return true;
        }

        final PrivateClaims privateClaims = tokenDecoder.decode(TokenType.ACCESS, token)
                                                        .orElseThrow(() ->
                                                                new InvalidTokenException("유효한 토큰이 아닙니다."));

        store.set(new AuthenticationUserInfo(privateClaims.userId()));
        return true;
    }

    private boolean isNotRequiredAuthenticate(final String token) {
        return token == null || token.length() == 0;
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
