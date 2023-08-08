package com.ddang.ddang.authentication.domain;

import java.time.LocalDateTime;
import java.util.Map;

public interface TokenEncoder {

    String encode(final LocalDateTime targetTime, final TokenType tokenType, final Map<String, Object> privateClaims);
}
