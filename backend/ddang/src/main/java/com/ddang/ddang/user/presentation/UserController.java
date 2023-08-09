package com.ddang.ddang.user.presentation;

import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.presentation.dto.request.LoginUserRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUser;
import com.ddang.ddang.user.application.UserService;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.dto.ReadUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ReadUserResponse> readById(
            @LoginUser final LoginUserRequest userRequest
    ) {
        final ReadUserDto readUserDto = userService.readById(LoginUserDto.from(userRequest));
        final ReadUserResponse response = ReadUserResponse.from(readUserDto);

        return ResponseEntity.ok(response);
    }
}
