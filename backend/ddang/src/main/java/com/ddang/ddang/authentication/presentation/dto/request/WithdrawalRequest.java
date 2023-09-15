package com.ddang.ddang.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record WithdrawalRequest(@NotEmpty(message = "refreshToken을 입력해주세요.") String refreshToken) {
}
