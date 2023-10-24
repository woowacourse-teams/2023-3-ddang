package com.ddang.ddang.user.domain;

import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class OauthInformation {

    private String oauthId;

    @Column(name = "oauth2_type")
    @Enumerated(EnumType.STRING)
    private Oauth2Type oauth2Type;

    public OauthInformation(final String oauthId, final Oauth2Type oauth2Type) {
        this.oauthId = oauthId;
        this.oauth2Type = oauth2Type;
    }
}
