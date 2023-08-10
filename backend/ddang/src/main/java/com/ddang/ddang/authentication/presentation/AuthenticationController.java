package com.ddang.ddang.authentication.presentation;

import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.presentation.dto.request.AccessTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.RefreshTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.response.TokenResponse;
import com.ddang.ddang.authentication.presentation.dto.response.ValidatedTokenResponse;
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

    @PostMapping("/login/{oauth2Type}")
    public ResponseEntity<Object> validate(
            @PathVariable Oauth2Type oauth2Type,
            @RequestBody final AccessTokenRequest request
    ) {
        final TokenDto tokenDto = authenticationService.login(oauth2Type, request.accessToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody final RefreshTokenRequest request) {
        final TokenDto tokenDto = authenticationService.refreshToken(request.refreshToken());

        return ResponseEntity.ok(TokenResponse.from(tokenDto));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ValidatedTokenResponse> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    ) {
        final boolean validated = authenticationService.validateToken(accessToken);

        return ResponseEntity.ok(new ValidatedTokenResponse(validated));
    }
}
