package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.image.util.ImageBaseUrl;
import com.ddang.ddang.image.util.ImageUrlBuilder;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {

    public static ReadChatPartnerResponse from(final ReadUserInChatRoomDto dto) {
        return new ReadChatPartnerResponse(dto.id(), dto.name(), convertImageUrl(dto.profileImageId()));
    }

    private static String convertImageUrl(final Long id) {
        return ImageUrlBuilder.calculate(ImageBaseUrl.USER, id);
    }
}
