package com.ddang.ddang.device.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device-token")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    @PatchMapping
    ResponseEntity<Void> update(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final UpdateDeviceTokenRequest updateDeviceTokenRequest
    ) {
        deviceTokenService.persist(userInfo.userId(), PersistDeviceTokenDto.from(updateDeviceTokenRequest));

        return ResponseEntity.ok()
                             .build();
    }
}
