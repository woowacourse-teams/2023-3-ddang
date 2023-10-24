package com.ddang.ddang.authentication.application;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.exception.EmptyTokenException;
import com.ddang.ddang.authentication.domain.repository.BlackListTokenRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListTokenService {

    private final BlackListTokenRepository blackListTokenRepository;
    private final TokenDecoder tokenDecoder;

    @Transactional
    public void registerBlackListToken(final String accessToken, final String refreshToken) {
        validateEmptyToken(accessToken, refreshToken);

        final List<BlackListToken> blackListTokens = new ArrayList<>();

        if (isValidToken(TokenType.ACCESS, accessToken)) {
            blackListTokens.add(new BlackListToken(TokenType.ACCESS, accessToken));
        }
        if (isValidToken(TokenType.REFRESH, refreshToken)) {
            blackListTokens.add(new BlackListToken(TokenType.REFRESH, refreshToken));
        }

        blackListTokenRepository.saveAll(blackListTokens);
    }

    private void validateEmptyToken(final String accessToken, final String refreshToken) {
        if (isEmptyToken(accessToken) || isEmptyToken(refreshToken)) {
            throw new EmptyTokenException("비어있는 토큰입니다.");
        }
    }

    private boolean isValidToken(final TokenType tokenType, final String targetToken) {
        return tokenDecoder.decode(tokenType, targetToken).isPresent();
    }

    private boolean isEmptyToken(final String targetToken) {
        return targetToken == null || targetToken.isBlank();
    }

    public boolean existsBlackListToken(final TokenType tokenType, final String targetToken) {
        return blackListTokenRepository.existsByTokenTypeAndToken(tokenType, targetToken);
    }
}
