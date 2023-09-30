package com.ddang.ddang.authentication.application.fixture;

import com.ddang.ddang.authentication.domain.BlackListToken;
import com.ddang.ddang.authentication.domain.TokenEncoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.infrastructure.persistence.JpaBlackListTokenRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class BlackListTokenServiceFixture {

    @Autowired
    private JpaBlackListTokenRepository blackListTokenRepository;

    @Autowired
    private TokenEncoder tokenEncoder;

    protected String 유효한_액세스_토큰;
    protected String 유효한_리프레시_토큰;
    protected String 만료된_액세스_토큰;
    protected String 만료된_리프레시_토큰;

    @BeforeEach
    void setUp() {
        유효한_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", 1L)
        );
        유효한_리프레시_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of("userId", 1L)
        );
        만료된_액세스_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.ACCESS,
                Map.of("userId", 2L)
        );
        만료된_리프레시_토큰 = tokenEncoder.encode(
                LocalDateTime.now(),
                TokenType.REFRESH,
                Map.of("userId", 2L)
        );

        blackListTokenRepository.save(new BlackListToken(TokenType.ACCESS, 만료된_액세스_토큰));
        blackListTokenRepository.save(new BlackListToken(TokenType.REFRESH, 만료된_리프레시_토큰));
    }
}
