package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {

    public static ReadChatPartnerResponse of(final ReadUserInChatRoomDto dto, final String imageRelativeUrl) {
        return new ReadChatPartnerResponse(
                dto.id(),
                dto.name(),
                imageRelativeUrl + dto.profileImageStoreName()
        );
    }
}
