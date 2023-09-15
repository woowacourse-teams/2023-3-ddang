package com.ddang.ddang.user.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.user.application.UserService;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.dto.UpdateUserDto;
import com.ddang.ddang.user.presentation.dto.ReadUserResponse;
import com.ddang.ddang.user.presentation.dto.request.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping
    public ResponseEntity<ReadUserResponse> readById(@AuthenticateUser final AuthenticationUserInfo userInfo) {
        final ReadUserDto readUserDto = userService.readById(userInfo.userId());
        final ReadUserResponse response = ReadUserResponse.from(readUserDto);

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<ReadUserResponse> updateById(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestPart @Valid final UpdateUserRequest request,
            @RequestPart final MultipartFile profileImage
    ) {
        final ReadUserDto readUserDto = userService.updateById(
                userInfo.userId(),
                UpdateUserDto.of(request, profileImage)
        );
        final ReadUserResponse response = ReadUserResponse.from(readUserDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<Void> delete(@AuthenticateUser final AuthenticationUserInfo userInfo) {
        userService.deleteById(userInfo.userId());

        return ResponseEntity.noContent()
                             .build();
    }
}
