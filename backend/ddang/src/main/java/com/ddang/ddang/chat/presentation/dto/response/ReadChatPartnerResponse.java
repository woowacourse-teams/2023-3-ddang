package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {

    public static ReadChatPartnerResponse from(final ReadUserInChatRoomDto dto) {
        final String name = NameProcessor.process(dto.isDeleted(), dto.name());
        return new ReadChatPartnerResponse(dto.id(), name, dto.profileImage());
    }
}
