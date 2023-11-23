package com.ddang.ddang.user.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrlFinder;
import com.ddang.ddang.image.presentation.util.ImageTargetType;
import com.ddang.ddang.user.application.UserService;
import com.ddang.ddang.user.application.dto.request.ReadUserDto;
import com.ddang.ddang.user.presentation.dto.response.ReadUserResponse;
import com.ddang.ddang.user.application.dto.response.UpdateUserDto;
import com.ddang.ddang.user.presentation.dto.request.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ImageRelativeUrlFinder urlFinder;

    @GetMapping
    public ResponseEntity<ReadUserResponse> readById(@AuthenticateUser final AuthenticationUserInfo userInfo) {
        final ReadUserDto readUserDto = userService.readById(userInfo.userId());
        final ReadUserResponse response = ReadUserResponse.of(
                readUserDto,
                urlFinder.find(ImageTargetType.PROFILE_IMAGE)
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<ReadUserResponse> updateById(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestPart(required = false) @Valid final UpdateUserRequest request,
            @RequestPart(required = false) final MultipartFile profileImage
    ) {
        UpdateUserDto updateUserDto = null;

        if (request != null) {
            updateUserDto = UpdateUserDto.of(request, profileImage);
        }

        final ReadUserDto readUserDto = userService.updateById(userInfo.userId(), updateUserDto);
        final ReadUserResponse response = ReadUserResponse.of(
                readUserDto,
                urlFinder.find(ImageTargetType.PROFILE_IMAGE)
        );

        return ResponseEntity.ok(response);
    }
}
