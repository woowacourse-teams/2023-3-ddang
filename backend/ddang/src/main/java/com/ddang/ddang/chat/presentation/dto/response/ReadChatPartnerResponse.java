package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserDto;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {
    public static ReadChatPartnerResponse from(final ReadUserDto dto) {
        return new ReadChatPartnerResponse(dto.id(), dto.name(), dto.profileImage());
    }
}
