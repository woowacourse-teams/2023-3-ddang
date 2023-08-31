package com.ddang.ddang.authentication.domain;

import com.ddang.ddang.authentication.domain.exception.EmptyTokenException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "tokenType", "token"})
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(length = 200, nullable = false)
    private String token;

    public BlackListToken(final TokenType tokenType, final String token) {
        validateToken(token);

        this.tokenType = tokenType;
        this.token = token;
    }

    private void validateToken(final String targetToken) {
        if (targetToken == null || targetToken.isBlank()) {
            throw new EmptyTokenException("비어있는 토큰입니다.");
        }
    }
}
