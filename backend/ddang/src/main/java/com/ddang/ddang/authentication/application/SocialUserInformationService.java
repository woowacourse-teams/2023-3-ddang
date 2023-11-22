package com.ddang.ddang.authentication.application;

import com.ddang.ddang.authentication.application.dto.response.SocialUserInfoDto;
import com.ddang.ddang.authentication.domain.Oauth2UserInformationProviderComposite;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialUserInformationService {

    private final Oauth2UserInformationProviderComposite providerComposite;

    public SocialUserInfoDto findInformation(final Oauth2Type oauth2Type, final String oauth2AccessToken) {
        final OAuth2UserInformationProvider provider = providerComposite.findProvider(oauth2Type);
        final UserInformationDto userInformation = provider.findUserInformation(oauth2AccessToken);

        return SocialUserInfoDto.from(userInformation);
    }
}
