package com.ddang.ddang.authentication.infrastructure.oauth2.kakao;

import com.ddang.ddang.authentication.configuration.KakaoProvidersConfigurationProperties;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.infrastructure.oauth2.OAuth2UserInformationProvider;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoUserInformationProvider implements OAuth2UserInformationProvider {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String KAKAO_ADMIN_TOKEN_TYPE = "KakaoAK ";
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

            throw new InvalidTokenException(message, ex);
        }
    }

    @Override
    public UserInformationDto unlinkUserBy(final String oauthId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, KAKAO_ADMIN_TOKEN_TYPE + providersConfigurationProperties.adminKey());

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", oauthId);

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            final ResponseEntity<UserInformationDto> response = restTemplate.exchange(
                    providersConfigurationProperties.userUnlinkUri(),
                    HttpMethod.POST,
                    request,
                    UserInformationDto.class
            );

            return response.getBody();
        } catch (final HttpClientErrorException ex) {
            final String message = ex.getMessage().split(REST_TEMPLATE_MESSAGE_SEPARATOR)[MESSAGE_INDEX];

            throw new InvalidTokenException(message, ex);
        }
    }
}
