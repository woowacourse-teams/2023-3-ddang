package com.ddang.ddang.authentication.infrastructure.oauth2;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import java.util.Locale;

public enum Oauth2Type {

    KAKAO;

    public static Oauth2Type from(final String typeName) {
        try {
            return Oauth2Type.valueOf(typeName.toUpperCase(Locale.ENGLISH));
        } catch (final IllegalArgumentException ex) {
            throw new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다.", ex);
        }
    }

    public String calculateNickname(final UserInformationDto dto) {
        return this.name()
                   .toLowerCase(Locale.ENGLISH)
                   .concat(String.valueOf(dto.id()));
    }
}
