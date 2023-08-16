package com.ddang.ddang.authentication.domain.dto;

public record AuthenticationUserInfo(Long userId) {

    public boolean isEmpty() {
        return this.userId == null;
    }
}
