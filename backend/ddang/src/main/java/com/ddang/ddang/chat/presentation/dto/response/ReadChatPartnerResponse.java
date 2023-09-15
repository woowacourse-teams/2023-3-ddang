package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageBaseUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record ReadChatPartnerResponse(Long id, String name, String profileImage) {

    public static ReadChatPartnerResponse from(final ReadUserInChatRoomDto dto) {
        return new ReadChatPartnerResponse(dto.id(), dto.name(), convertImageUrl(dto.profileImageId()));
    }

    private static String convertImageUrl(final Long id) {
        return ImageUrlCalculator.calculate(ImageBaseUrl.USER, id);
    }
}
