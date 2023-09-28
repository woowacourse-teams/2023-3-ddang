package com.ddang.ddang.authentication.infrastructure.jwt.fixture;

import com.ddang.ddang.authentication.configuration.JwtConfigurationProperties;
import java.util.Map;

public class JwtEncoderFixture {

    protected JwtConfigurationProperties 토큰_설정 = new JwtConfigurationProperties(
            "thisistoolargeaccesstokenkeyfordummykeydataforlocal",
            "thisistoolargerefreshtokenkeyfordummykeydataforlocal",
            12L,
            1460L
    );
    protected final Map<String, Object> 토큰_내용 = Map.of("userId", 1L);
}
