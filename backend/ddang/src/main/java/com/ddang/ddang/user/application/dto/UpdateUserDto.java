package com.ddang.ddang.user.application.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDto(String name, MultipartFile profileImage) {
}
