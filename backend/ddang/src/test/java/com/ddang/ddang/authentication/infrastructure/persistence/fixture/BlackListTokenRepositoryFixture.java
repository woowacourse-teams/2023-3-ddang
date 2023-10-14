package com.ddang.ddang.authentication.infrastructure.persistence.fixture;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.repository.BlackListTokenRepository;
import com.ddang.ddang.authentication.infrastructure.persistence.BlackListTokenRepositoryImpl;
import com.ddang.ddang.authentication.infrastructure.persistence.JpaBlackListTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class BlackListTokenRepositoryFixture {

    @Autowired
    private JpaBlackListTokenRepository jpaBlackListTokenRepository;

    protected String 만료_토큰_내용 = "expired token";
    protected String 만료되지_않은_토큰_내용 = "not expired token";
    protected BlackListToken 만료할_토큰1 = new BlackListToken(TokenType.ACCESS, 만료되지_않은_토큰_내용);
    protected BlackListToken 만료할_토큰2 = new BlackListToken(TokenType.ACCESS, 만료되지_않은_토큰_내용);
    protected BlackListToken 만료된_토큰 = new BlackListToken(TokenType.ACCESS, 만료_토큰_내용);

    @BeforeEach
    void setUp() {
        final BlackListTokenRepository blackListTokenRepository =
                new BlackListTokenRepositoryImpl(jpaBlackListTokenRepository);

        blackListTokenRepository.save(만료된_토큰);
    }
}
