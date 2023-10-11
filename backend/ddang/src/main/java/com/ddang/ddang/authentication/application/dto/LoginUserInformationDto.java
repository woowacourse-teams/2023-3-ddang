package com.ddang.ddang.authentication.application.dto;

import com.ddang.ddang.user.domain.User;

public record LoginUserInformationDto(User user, boolean persisted) {
}
