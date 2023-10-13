package com.ddang.ddang.authentication.infrastructure.persistence;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.repository.BlackListTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackListTokenRepositoryImpl implements BlackListTokenRepository {

    private final JpaBlackListTokenRepository jpaBlackListTokenRepository;

    @Override
    public BlackListToken save(final BlackListToken blackListToken) {
        return jpaBlackListTokenRepository.save(blackListToken);
    }

    @Override
    public List<BlackListToken> saveAll(final List<BlackListToken> blackListTokens) {
        return jpaBlackListTokenRepository.saveAll(blackListTokens);
    }

    @Override
    public boolean existsByTokenTypeAndToken(final TokenType tokenType, final String accessToken) {
        return jpaBlackListTokenRepository.existsByTokenTypeAndToken(tokenType, accessToken);
    }
}
