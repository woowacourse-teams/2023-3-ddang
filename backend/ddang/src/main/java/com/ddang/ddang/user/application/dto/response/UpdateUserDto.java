package com.ddang.ddang.user.application.dto.response;

import com.ddang.ddang.user.presentation.dto.request.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDto(String name, MultipartFile profileImage) {

    public static UpdateUserDto of(final UpdateUserRequest userRequest, final MultipartFile profileImage) {
        return new UpdateUserDto(userRequest.name(), profileImage);
    }
}
