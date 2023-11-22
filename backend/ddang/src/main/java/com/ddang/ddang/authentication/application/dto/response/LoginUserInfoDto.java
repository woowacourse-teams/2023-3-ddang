package com.ddang.ddang.authentication.application.dto.response;

import com.ddang.ddang.user.domain.User;

public record LoginUserInfoDto(User user, boolean persisted) {
}
