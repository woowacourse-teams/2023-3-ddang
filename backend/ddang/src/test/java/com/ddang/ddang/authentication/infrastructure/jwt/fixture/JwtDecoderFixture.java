package com.ddang.ddang.authentication.infrastructure.jwt.fixture;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.infrastructure.jwt.JwtEncoder;
import java.time.LocalDateTime;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class JwtDecoderFixture {

    protected String 유효하지_않은_길이의_토큰 = "abcde";
    protected String 유효하지_않은_토큰 = "Bearer abcde";
    protected String 유효하지_않은_타입의_토큰 = "Basic12 abcde";
    protected JwtConfigurationProperties 토큰_설정 = new JwtConfigurationProperties(
            "thisistoolargeaccesstokenkeyfordummykeydataforlocal",
            "thisistoolargerefreshtokenkeyfordummykeydataforlocal",
            12L,
            1460L
    );
    protected JwtEncoder 토큰_암호화_생성기 = new JwtEncoder(토큰_설정);
    protected Map<String, Object> 토큰_내용 = Map.of("userId", 1L);
    protected String 유효한_토큰 = 토큰_암호화_생성기.encode(LocalDateTime.now(), TokenType.ACCESS, 토큰_내용);
    protected String 회원_아이디_키 = "userId";
}
