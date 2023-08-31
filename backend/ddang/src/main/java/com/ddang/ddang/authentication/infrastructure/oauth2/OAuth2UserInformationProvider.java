package com.ddang.ddang.authentication.infrastructure.oauth2;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;

public interface OAuth2UserInformationProvider {

    Oauth2Type supportsOauth2Type();

    UserInformationDto findUserInformation(final String accessToken);
}
