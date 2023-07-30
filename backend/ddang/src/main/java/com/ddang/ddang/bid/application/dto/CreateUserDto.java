package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.bid.presentation.dto.CreateUserRequest;

public record CreateUserDto(Long usedId) {

    public static CreateUserDto from(final CreateUserRequest userRequest) {
        return new CreateUserDto(userRequest.userId());
    }
}
