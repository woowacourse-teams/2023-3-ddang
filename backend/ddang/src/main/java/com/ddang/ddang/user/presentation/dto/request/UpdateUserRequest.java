package com.ddang.ddang.user.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRequest(@NotEmpty(message = "수정하려는 이름이 입력되지 않았습니다.") String name) {
}
