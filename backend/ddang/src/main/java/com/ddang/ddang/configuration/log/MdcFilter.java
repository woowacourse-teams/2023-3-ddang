package com.ddang.ddang.configuration.log;

import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class MdcFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER_KEY = "X-Request-Id";
    private static final String UNAUTHORIZED_USER_ID = "Unauthorized";

    private final TokenDecoder tokenDecoder;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        MDC.put("requestId", findRequestId(request));
        MDC.put("requestUri", request.getRequestURI());
        MDC.put("userId", String.valueOf(findUserId(request)));

        filterChain.doFilter(request, response);

        MDC.clear();
    }

    private String findRequestId(final HttpServletRequest request) {
        final String requestId = request.getHeader(REQUEST_ID_HEADER_KEY);

        if (requestId == null || requestId.isEmpty() || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return requestId;
    }

    private String findUserId(final HttpServletRequest request) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            return tokenDecoder.decode(TokenType.ACCESS, accessToken)
                               .map(privateClaims -> String.valueOf(privateClaims.userId()))
                               .orElse(UNAUTHORIZED_USER_ID);
        } catch (final InvalidTokenException ex) {
            return UNAUTHORIZED_USER_ID;
        }
    }
}
