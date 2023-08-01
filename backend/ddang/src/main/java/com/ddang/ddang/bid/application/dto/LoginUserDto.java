package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.bid.presentation.dto.LoginUserRequest;

public record LoginUserDto(Long usedId) {

    public static LoginUserDto from(final LoginUserRequest userRequest) {
        return new LoginUserDto(userRequest.userId());
    }
}
