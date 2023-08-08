package com.ddang.ddang.authentication.domain.dto;

public record UserInformationDto(Long id) {

    public String findUserId() {
        return String.valueOf(id);
    }
}
