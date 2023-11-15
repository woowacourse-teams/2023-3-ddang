package com.ddang.ddang.authentication.presentation;

import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.application.SocialUserInformationService;
import com.ddang.ddang.authentication.application.dto.LoginInformationDto;
import com.ddang.ddang.authentication.application.dto.SocialUserInformationDto;
import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.presentation.dto.request.LoginTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.LogoutRequest;
import com.ddang.ddang.authentication.presentation.dto.request.RefreshTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.WithdrawalRequest;
import com.ddang.ddang.authentication.presentation.dto.response.LoginInformationResponse;
import com.ddang.ddang.authentication.presentation.dto.response.TokenResponse;
import com.ddang.ddang.authentication.presentation.dto.response.ValidatedTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SocialUserInformationService socialUserInformationService;
    private final BlackListTokenService blackListTokenService;

    @PostMapping("/login/{oauth2Type}")
    public ResponseEntity<LoginInformationResponse> login(
            @PathVariable final Oauth2Type oauth2Type,
            @RequestBody final LoginTokenRequest request
    ) {
        final SocialUserInformationDto socialUserInformationDto =
                socialUserInformationService.findInformation(oauth2Type, request.accessToken());
        final LoginInformationDto loginInformationDto =
                authenticationService.login(socialUserInformationDto.id(), oauth2Type, request.deviceToken());

        return ResponseEntity.ok(LoginInformationResponse.from(loginInformationDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody final RefreshTokenRequest request) {
        final TokenDto tokenDto = authenticationService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ValidatedTokenResponse> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken
    ) {
        final boolean validated = authenticationService.validateToken(accessToken);

        return ResponseEntity.ok(new ValidatedTokenResponse(validated));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestBody @Valid final LogoutRequest request
    ) {
        blackListTokenService.registerBlackListToken(accessToken, request.refreshToken());

        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<Void> withdrawal(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestBody @Valid final WithdrawalRequest request
    ) {
        authenticationService.withdrawal(accessToken, request.refreshToken());

        return ResponseEntity.noContent()
                             .build();
    }
}
