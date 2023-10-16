package com.ddang.ddang.authentication.domain.repository;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import java.util.List;

public interface BlackListTokenRepository {

    BlackListToken save(final BlackListToken blackListToken);

    List<BlackListToken> saveAll(final List<BlackListToken> blackListTokens);

    boolean existsByTokenTypeAndToken(final TokenType tokenType, final String accessToken);
}
