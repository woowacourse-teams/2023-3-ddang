package com.ddang.ddang.authentication.infrastructure.oauth2.kakao;

import com.ddang.ddang.authentication.configuration.KakaoProvidersConfigurationProperties;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.infrastructure.oauth2.exception.InvalidSocialOauth2TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoUserInformationProvider implements OAuth2UserInformationProvider {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String REST_TEMPLATE_MESSAGE_SEPARATOR = ":";
    private static final int MESSAGE_INDEX = 0;

    private final RestTemplate restTemplate;
    private final KakaoProvidersConfigurationProperties providersConfigurationProperties;

    @Override
    public Oauth2Type supportsOauth2Type() {
        return Oauth2Type.KAKAO;
    }

    @Override
    public UserInformationDto findUserInformation(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + accessToken);

        final HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);

        try {
            final ResponseEntity<UserInformationDto> response = restTemplate.exchange(
                    providersConfigurationProperties.userInfoUri(),
                    HttpMethod.GET,
                    request,
                    UserInformationDto.class
            );

            return response.getBody();
        } catch (final HttpClientErrorException ex) {
            final String message = ex.getMessage().split(REST_TEMPLATE_MESSAGE_SEPARATOR)[MESSAGE_INDEX];

            throw new InvalidSocialOauth2TokenException(message, ex);
        }
    }
}
