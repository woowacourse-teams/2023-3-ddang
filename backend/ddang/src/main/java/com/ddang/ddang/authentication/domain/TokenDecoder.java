package com.ddang.ddang.authentication.domain;

import java.util.Optional;

public interface TokenDecoder {

    Optional<PrivateClaims> decode(final TokenType tokenType, final String token);
}
